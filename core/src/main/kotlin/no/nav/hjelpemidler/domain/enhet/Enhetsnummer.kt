package no.nav.hjelpemidler.domain.enhet

import no.nav.hjelpemidler.domain.id.StringId
import no.nav.hjelpemidler.text.isInteger
import no.nav.hjelpemidler.validation.Validator

class Enhetsnummer(value: String) : StringId(value) {
    init {
        require(erGyldig(value)) { "Ugyldig enhetsnummer: '$value'" }
    }

    companion object : Validator<String> {
        private const val LENGTH = 4

        override fun erGyldig(value: String): Boolean = value.length == LENGTH && value.isInteger()
    }
}
