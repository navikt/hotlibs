package no.nav.hjelpemidler.domain.enhet

import no.nav.hjelpemidler.domain.id.StringId
import no.nav.hjelpemidler.text.isInteger
import java.sql.Types

class Enhetsnummer(value: String) : StringId(value) {
    init {
        require(erGyldig(value)) { "Ugyldig enhetsnummer: '$value'" }
    }

    companion object {
        const val SQL_TYPE: Int = Types.CHAR

        fun erGyldig(value: String): Boolean = value.length == 4 && value.isInteger()
    }
}
