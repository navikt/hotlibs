package no.nav.hjelpemidler.oppgave.hendelse

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.domain.tilgang.NavIdent
import no.nav.hjelpemidler.domain.tilgang.UtførtAvId
import java.time.LocalDate
import java.time.LocalDateTime

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
)

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
    val isTemaHjelpemidler: Boolean @JsonIgnore get() = tema == "HJE"

    val isOppgavetypeJournalføring: Boolean @JsonIgnore get() = oppgavetype == Oppgavetype.JOURNALFØRING
    val isOppgavetypeBehandleSak: Boolean @JsonIgnore get() = oppgavetype == Oppgavetype.BEHANDLE_SAK
    val isOppgavetypeGodkjenneVedtak: Boolean @JsonIgnore get() = oppgavetype == Oppgavetype.GODKJENNE_VEDTAK
    val isOppgavetypeBehandleUnderkjentVedtak: Boolean @JsonIgnore get() = oppgavetype == Oppgavetype.BEHANDLE_UNDERKJENT_VEDTAK

    val isOppgavetypeHotsak: Boolean @JsonIgnore get() = oppgavetype in Oppgavetype.HOTSAK

    enum class Prioritet {
        @JsonAlias("HOY")
        HØY,
        NORMAL,
        LAV,
    }

    private object Oppgavetype {
        const val JOURNALFØRING = "JFR"
        const val BEHANDLE_SAK = "BEH_SAK"
        const val GODKJENNE_VEDTAK = "GOD_VED"
        const val BEHANDLE_UNDERKJENT_VEDTAK = "BEH_UND_VED"

        val HOTSAK: Set<String> = setOf(
            JOURNALFØRING,
            BEHANDLE_SAK,
            GODKJENNE_VEDTAK,
            BEHANDLE_UNDERKJENT_VEDTAK,
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
    enum class IdentType {
        FOLKEREGISTERIDENT,
        NPID,
        ORGNR,
        SAMHANDLERNR,
    }
}
