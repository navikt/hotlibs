package no.nav.hjelpemidler.domain.geografi

import com.fasterxml.jackson.annotation.JsonAlias
import no.nav.hjelpemidler.text.isInteger

class Bydel(
    @param:JsonAlias("bydelsnummer")
    override val nummer: String,
    @param:JsonAlias("bydelsnavn")
    override val navn: String,
) : GeografiskEnhet() {
    init {
        require(erGyldig(nummer)) { "Ugyldig bydelsnummer: '$nummer'" }
    }

    /**
     * NB! Ikke alle bydeler er lagt til her pt. Kun de som brukes i logikk og/eller tester.
     */
    companion object {
        fun erGyldig(value: String): Boolean = value.length == 6 && value.isInteger()

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/bydeler/030105">DigiHoT Oppslag</a>
         */
        val FROGNER =
            Bydel("030105", "Frogner")

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/bydeler/460105">DigiHoT Oppslag</a>
         */
        val LAKSEVÅG =
            Bydel("460105", "Laksevåg")
    }
}
