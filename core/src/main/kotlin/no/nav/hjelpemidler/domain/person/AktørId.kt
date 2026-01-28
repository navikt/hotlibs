package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.text.isInteger
import no.nav.hjelpemidler.validation.Validator

/**
 * AktørId med 13 siffer.
 */
class AktørId(value: String) : PersonIdent(value) {
    init {
        require(erGyldig(value)) { "Ugyldig aktørId" }
    }

    companion object : Validator<String> {
        private const val LENGTH = 13

        override fun erGyldig(value: String): Boolean = value.length == LENGTH && value.isInteger()
    }
}

/**
 * Konverter til [AktørId].
 */
fun String.toAktørId(): AktørId = AktørId(this)
