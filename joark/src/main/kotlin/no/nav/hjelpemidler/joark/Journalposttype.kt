package no.nav.hjelpemidler.joark

import com.fasterxml.jackson.annotation.JsonAlias
import no.nav.hjelpemidler.domain.kodeverk.Kodeverk

enum class Journalposttype(override val beskrivelse: String) : Kodeverk<Journalposttype> {
    @JsonAlias("I")
    INNGÅENDE(beskrivelse = "Inngående"),

    @JsonAlias("U")
    UTGÅENDE(beskrivelse = "Utgående"),

    @JsonAlias("N")
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
