package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.text.isInteger
import java.sql.Types

/**
 * AktørId med 13 siffer.
 */
class AktørId(value: String) : PersonIdent(value) {
    init {
        if (!erGyldig(value)) {
            throw IllegalArgumentException("Ugyldig aktørId")
        }
    }

    companion object {
        const val SQL_TYPE: Int = Types.CHAR

        fun erGyldig(value: String): Boolean = value.length == 13 && value.isInteger()
    }
}

/**
 * Konverter til [AktørId].
 */
fun String.toAktørId(): AktørId = AktørId(this)
