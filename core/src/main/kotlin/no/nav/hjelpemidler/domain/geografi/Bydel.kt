package no.nav.hjelpemidler.domain.geografi

import com.fasterxml.jackson.annotation.JsonAlias

data class Bydel(
    @JsonAlias("bydelsnummer")
    override val nummer: String,
    @JsonAlias("bydelsnavn")
    override val navn: String,
) : GeografiskOmråde() {
    /**
     * NB! Ikke alle bydeler er lagt til her pt. Kun de som brukes i logikk og/eller tester.
     */
    companion object {
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
