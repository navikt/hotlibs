package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.logging.secureLog
import no.nav.hjelpemidler.text.isInteger

/**
 * AktørId med 13 siffer.
 */
class AktørId(value: String) : PersonIdent(value) {
    init {
        if (!erGyldig(value)) {
            secureLog.error { "Ugyldig aktørId: '$value'" }
            throw IllegalArgumentException("Ugyldig aktørId")
        }
    }

    companion object {
        fun erGyldig(value: String): Boolean = value.length == 13 && value.isInteger()
    }
}

/**
 * Konverter til [AktørId].
 */
fun String.toAktørId(): AktørId = AktørId(this)
