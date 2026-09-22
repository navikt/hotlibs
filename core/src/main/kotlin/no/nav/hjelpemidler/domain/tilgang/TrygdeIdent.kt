package no.nav.hjelpemidler.domain.tilgang

import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.text.isInteger
import no.nav.hjelpemidler.validation.Validator

/**
 * Trygde-ident med følgende format: `XYZ2990`
 *
 * NB! Dette er en utgått, legacy-ident som bla. kan oppstå som `tildeltRessurs` på gamle oppgaver.
 */
class TrygdeIdent(value: String) : UtførtAvId(value.uppercase()), AnsattId {
    init {
        require(erGyldig(value)) { "Ugyldig Trygde-ident: '$value'" }
    }

    val enhet: Enhetsnummer @JsonIgnore get() = Enhetsnummer(value.takeLast(4))

    companion object : Validator<String> {
        private const val LENGTH = 7
        internal val FIRST_CHARACTER_RANGE: CharRange = 'A'..'Z'

        override fun erGyldig(value: String): Boolean =
            value.length == LENGTH
                    && value.take(3).all { it.uppercaseChar() in FIRST_CHARACTER_RANGE }
                    && value.drop(3).isInteger()
    }
}
