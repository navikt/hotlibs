package no.nav.hjelpemidler.joark

import com.fasterxml.jackson.annotation.JsonAlias
import no.nav.hjelpemidler.domain.kodeverk.Kodeverk

enum class Journalstatus : Kodeverk<Journalstatus> {
    MOTTATT,

    @JsonAlias("JOURNALFOERT")
    JOURNALFØRT,
    FERDIGSTILT,
    EKSPEDERT,
    UNDER_ARBEID,
    FEILREGISTRERT,

    @JsonAlias("UTGAAR")
    UTGÅR,
    AVBRUTT,
    UKJENT_BRUKER,
    RESERVERT,
    OPPLASTING_DOKUMENT,
    UKJENT,
    ;

    companion object {
        fun of(name: String): Journalstatus = when (name) {
            "JOURNALFOERT" -> JOURNALFØRT
            "UTGAAR" -> UTGÅR
            else -> enumValueOf<Journalstatus>(name)
        }
    }
}
