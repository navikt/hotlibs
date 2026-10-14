package no.nav.hjelpemidler.joark

import no.nav.hjelpemidler.domain.kodeverk.Kanal
import no.nav.hjelpemidler.domain.kodeverk.Tema

/**
 * @see <a href="https://confluence.adeo.no/x/Ix-DGQ">Joarkhendelser - Team Dokumentløsninger Sysdok - Confluence</a>
 * @see <a href="https://github.com/navikt/teamdokumenthandtering-avro-schemas">navikt/teamdokumenthandtering-avro-schemas</a>
 */
data class Joarkhendelse(
    val id: String,
    val versjon: Int,
    val type: Type,
    val journalpostId: String,
    val journalstatus: Journalstatus,
    val temaGammelt: Tema?,
    val temaNytt: Tema,
    val kanal: Kanal,
    /**
     * Også kjent som `eksternReferanseId`.
     */
    val kanalReferanseId: EksternReferanseId,
    val behandlingstema: String?,
) {
    val isTypeJournalpostMottatt: Boolean get() = type == Type.JOURNALPOST_MOTTATT
    val isTypeJournalpostEndeligJournalført: Boolean get() = type == Type.ENDELIG_JOURNALFØRT

    val isTemaHjelpemidler: Boolean get() = temaNytt == Tema.HJE

    override fun toString(): String =
        "id: $id, versjon: $versjon, type: $type, journalpostId: $journalpostId, journalstatus: $journalstatus, tema: $temaNytt, kanal: $kanal, kanalReferanseId: '$kanalReferanseId', behandlingstema: $behandlingstema"

    enum class Type {
        JOURNALPOST_MOTTATT,
        TEMA_ENDRET,
        ENDELIG_JOURNALFØRT,
        JOURNALPOST_UTGÅTT,
        ;
    }
}
