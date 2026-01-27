package no.nav.hjelpemidler.domain.tilgang

import no.nav.hjelpemidler.text.isInteger

/**
 * Nav-ident med følgende format: `A123456`
 */
class NavIdent(value: String) : UtførtAvId(value) {
    init {
        if (!erGyldig(value)) {
            throw IllegalArgumentException("Ugyldig Nav-ident: '$value'")
        }
    }

    companion object {
        private val range: CharRange = 'A'..'Z'

        fun erGyldig(value: String): Boolean = value.length == 7 && value[0] in range && value.drop(1).isInteger()

        val UKJENT = NavIdent("Z999999")
    }
}
