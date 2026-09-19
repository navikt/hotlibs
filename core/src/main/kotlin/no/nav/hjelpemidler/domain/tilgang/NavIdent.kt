package no.nav.hjelpemidler.domain.tilgang

import no.nav.hjelpemidler.text.isInteger
import no.nav.hjelpemidler.validation.Validator

/**
 * Nav-ident med følgende format: `A123456`
 */
class NavIdent(value: String) : UtførtAvId(value.uppercase()) {
    init {
        require(erGyldig(value)) { "Ugyldig Nav-ident: '$value'" }
    }

    companion object : Validator<String> {
        private const val LENGTH = 7
        internal val FIRST_CHARACTER_RANGE: CharRange = 'A'..'Z'

        override fun erGyldig(value: String): Boolean =
            value.length == LENGTH && value[0].uppercaseChar() in FIRST_CHARACTER_RANGE && value.drop(1).isInteger()

        val UKJENT = NavIdent("Z999999")
    }
}
