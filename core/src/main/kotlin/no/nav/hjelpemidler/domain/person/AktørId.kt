package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.logging.secureLog

/**
 * AktørId med 13 siffer.
 */
@JvmInline
value class AktørId(override val value: String) : PersonIdent, CharSequence by value {
    init {
        if (!(value matches regex)) {
            secureLog.error { "Ugyldig aktørId: '$value'" }
            throw IllegalArgumentException("Ugyldig aktørId")
        }
    }

    override fun toString(): String = value
}

/**
 * Konverter til [AktørId].
 */
fun String.toAktørId(): AktørId = AktørId(this)

private val regex: Regex = Regex("^[0-9]{13}$")
