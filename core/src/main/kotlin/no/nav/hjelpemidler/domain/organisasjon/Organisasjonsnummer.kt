package no.nav.hjelpemidler.domain.organisasjon

import no.bekk.bekkopen.org.OrganisasjonsnummerValidator
import no.nav.hjelpemidler.domain.id.BrukerId
import no.nav.hjelpemidler.validation.Validator

/**
 * Organisasjonsnummer med 9 siffer.
 *
 * @see [no.bekk.bekkopen.org.Organisasjonsnummer]
 * @see [no.bekk.bekkopen.org.OrganisasjonsnummerValidator]
 */
class Organisasjonsnummer(value: String) : BrukerId(value) {
    init {
        require(erGyldig(value)) { "Ugyldig organisasjonsnummer: '$value'" }
    }

    companion object : Validator<String> {
        override fun erGyldig(value: String): Boolean = OrganisasjonsnummerValidator.isValid(value)
    }
}
