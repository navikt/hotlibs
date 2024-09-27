package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.logging.secureLog

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
        private val regex: Regex = Regex("^[0-9]{13}$")

        fun erGyldig(value: String): Boolean = value matches regex
    }
}

/**
 * Konverter til [AktørId].
 */
fun String.toAktørId(): AktørId = AktørId(this)
