package no.nav.hjelpemidler.domain.organisasjon

import no.nav.hjelpemidler.domain.id.StringId
import no.nav.hjelpemidler.text.isInteger
import no.nav.hjelpemidler.validation.Validator

/**
 * Organisasjonsnummer med 9 siffer.
 */
class Organisasjonsnummer(value: String) : StringId(value) {
    init {
        require(erGyldig(value)) { "Ugyldig organisasjonsnummer: '$value'" }
    }

    companion object : Validator<String> {
        private const val LENGTH = 9

        override fun erGyldig(value: String): Boolean = value.length == LENGTH && value.isInteger()
    }
}
