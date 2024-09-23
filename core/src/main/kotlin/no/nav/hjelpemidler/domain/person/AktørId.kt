package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.logging.secureLog

/**
 * AktørId med 13 siffer.
 */
class AktørId(value: String) : PersonIdent(value) {
    init {
        if (!(value matches regex)) {
            secureLog.error { "Ugyldig aktørId: '$value'" }
            throw IllegalArgumentException("Ugyldig aktørId")
        }
    }
}

/**
 * Konverter til [AktørId].
 */
fun String.toAktørId(): AktørId = AktørId(this)

private val regex: Regex = Regex("^[0-9]{13}$")
