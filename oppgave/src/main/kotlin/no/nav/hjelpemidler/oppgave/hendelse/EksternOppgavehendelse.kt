package no.nav.hjelpemidler.oppgave.hendelse

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.tilgang.NavIdent
import no.nav.hjelpemidler.domain.tilgang.UtførtAvId
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @see <a href="https://github.com/navikt/oppgave/blob/master/src/main/java/no/nav/oppgave/infrastruktur/kafka/OppgaveKafkaAivenRecord.java">OppgaveKafkaAivenRecord.java</a>
 */
data class EksternOppgavehendelse(
    val hendelse: Hendelse,
    @JsonAlias("utfortAv")
    val utførtAv: UtførtAv,
    val oppgave: Oppgave,
) {
    data class Hendelse(
        val hendelsestype: Type,
        val tidspunkt: LocalDateTime,
    ) {
        val isOpprettet: Boolean @JsonIgnore get() = hendelsestype == Type.OPPGAVE_OPPRETTET
        val isEndret: Boolean @JsonIgnore get() = hendelsestype == Type.OPPGAVE_ENDRET
        val isFerdigstilt: Boolean @JsonIgnore get() = hendelsestype == Type.OPPGAVE_FERDIGSTILT
        val isFeilregistrert: Boolean @JsonIgnore get() = hendelsestype == Type.OPPGAVE_FEILREGISTRERT

        enum class Type {
            OPPGAVE_OPPRETTET,
            OPPGAVE_ENDRET,
            OPPGAVE_FERDIGSTILT,
            OPPGAVE_FEILREGISTRERT,
        }
    }

    data class UtførtAv(
        @JsonAlias("ident", "navIdent")
        val id: UtførtAvId,
        @JsonAlias("enhetsnr")
        val enhetsnummer: Enhetsnummer?,
    )

    data class Oppgave(
        val oppgaveId: String,
        val versjon: Int,
        val tilordning: Tilordning?,
        val kategorisering: Kategorisering,
        val behandlingsperiode: Behandlingsperiode?,
        val bruker: Bruker?,
    ) {
        data class Tilordning(
            @JsonAlias("enhetsnr")
            val enhetsnummer: Enhetsnummer?,
            val enhetsmappeId: String?,
            val navIdent: NavIdent?,
        )

        data class Kategorisering(
            val tema: String,
            val oppgavetype: String,
            val behandlingstema: String?,
            val behandlingstype: String?,
            val prioritet: Prioritet,
        ) {
            val isTemaHjelpemidler: Boolean @JsonIgnore get() = tema == TEMA_HJE

            val isOppgavetypeJournalføring: Boolean @JsonIgnore get() = oppgavetype == OPPGAVETYPE_JOURNALFØRING
            val isOppgavetypeBehandleSak: Boolean @JsonIgnore get() = oppgavetype == OPPGAVETYPE_BEHANDLE_SAK
            val isOppgavetypeGodkjenneVedtak: Boolean @JsonIgnore get() = oppgavetype == OPPGAVETYPE_GODKJENNE_VEDTAK
            val isOppgavetypeBehandleUnderkjentVedtak: Boolean @JsonIgnore get() = oppgavetype == OPPGAVETYPE_BEHANDLE_UNDERKJENT_VEDTAK

            val isOppgavetypeHotsak: Boolean @JsonIgnore get() = oppgavetype in OPPGAVETYPER_HOTSAK

            enum class Prioritet {
                @JsonAlias("HOY")
                HØY,
                NORMAL,
                LAV,
            }

            internal companion object {
                const val TEMA_HJE = "HJE"

                const val OPPGAVETYPE_JOURNALFØRING = "JFR"
                const val OPPGAVETYPE_BEHANDLE_SAK = "BEH_SAK"
                const val OPPGAVETYPE_GODKJENNE_VEDTAK = "GOD_VED"
                const val OPPGAVETYPE_BEHANDLE_UNDERKJENT_VEDTAK = "BEH_UND_VED"

                val OPPGAVETYPER_HOTSAK: Set<String> = setOf(
                    OPPGAVETYPE_JOURNALFØRING,
                    OPPGAVETYPE_BEHANDLE_SAK,
                    OPPGAVETYPE_GODKJENNE_VEDTAK,
                    OPPGAVETYPE_BEHANDLE_UNDERKJENT_VEDTAK,
                )
            }
        }

        data class Behandlingsperiode(
            val aktiv: LocalDate,
            val frist: LocalDate?,
        )

        data class Bruker(
            val ident: String,
            val identType: IdentType,
        ) {
            val fnr: Fødselsnummer
                @JsonIgnore
                get() = if (identType == IdentType.FOLKEREGISTERIDENT) {
                    Fødselsnummer(ident)
                } else {
                    error("identType: $identType")
                }

            enum class IdentType {
                FOLKEREGISTERIDENT,
                NPID,
                ORGNR,
                SAMHANDLERNR,
            }
        }
    }
}
