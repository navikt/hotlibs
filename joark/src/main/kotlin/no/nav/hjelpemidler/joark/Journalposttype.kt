package no.nav.hjelpemidler.joark

import no.nav.hjelpemidler.domain.kodeverk.EksternKode
import no.nav.hjelpemidler.domain.kodeverk.EksterntKodeverk
import no.nav.hjelpemidler.domain.kodeverk.Kodeverk

enum class Journalposttype(override val beskrivelse: String) : Kodeverk<Journalposttype>, EksterntKodeverk {
    @EksternKode("I")
    INNGÅENDE(beskrivelse = "Inngående"),

    @EksternKode("U")
    UTGÅENDE(beskrivelse = "Utgående"),

    @EksternKode("N")
    NOTAT(beskrivelse = "Notat"),
    ;

    companion object {
        fun of(name: String): Journalposttype = when (name) {
            "I" -> INNGÅENDE
            "U" -> UTGÅENDE
            "N" -> NOTAT
            else -> enumValueOf<Journalposttype>(name)
        }
    }
}
