package no.nav.hjelpemidler.joark

import no.nav.hjelpemidler.domain.kodeverk.EksternKode
import no.nav.hjelpemidler.domain.kodeverk.EksterntKodeverk
import no.nav.hjelpemidler.domain.kodeverk.Kodeverk

enum class Journalstatus : Kodeverk<Journalstatus>, EksterntKodeverk {
    MOTTATT,

    @EksternKode("JOURNALFOERT")
    JOURNALFØRT,
    FERDIGSTILT,
    EKSPEDERT,
    UNDER_ARBEID,
    FEILREGISTRERT,

    @EksternKode("UTGAAR")
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
