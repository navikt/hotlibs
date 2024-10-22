package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.logging.secureLog
import no.nav.hjelpemidler.validering.nummerValidator

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
        private val validator = nummerValidator(13)

        fun erGyldig(value: String): Boolean = validator(value)
    }
}

/**
 * Konverter til [AktørId].
 */
fun String.toAktørId(): AktørId = AktørId(this)
