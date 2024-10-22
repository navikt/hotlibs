package no.nav.hjelpemidler.domain.geografi

import com.fasterxml.jackson.annotation.JsonAlias
import no.nav.hjelpemidler.validering.nummerValidator

class Enhet(
    /**
     * Alias: "enhetNr" er bla. brukt i NORG2.
     */
    @JsonAlias("enhetsnummer", "enhetNr")
    override val nummer: String,
    @JsonAlias("enhetsnavn")
    override val navn: String,
) : GeografiskOmråde() {
    init {
        require(validator(nummer)) { "Ugyldig enhetsnummer: '$nummer'" }
    }

    /**
     * NB! Ikke alle sentraler er lagt til her pt. Kun de som brukes i logikk og/eller tester.
     */
    companion object {
        private val validator = nummerValidator(lengde = 4)

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/2103">NORG2</a>
         */
        val NAV_VIKAFOSSEN =
            Enhet(nummer = "2103", navn = "NAV Vikafossen")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/2970">NORG2</a>
         */
        val IT_AVDELINGEN =
            Enhet(nummer = "2970", navn = "IT-avdelingen")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4303">NORG2</a>
         */
        val NAV_ID_OG_FORDELING =
            Enhet(nummer = "4303", navn = "NAV Id og fordeling")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4703">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_OSLO =
            Enhet(nummer = "4703", navn = "NAV Hjelpemiddelsentral Oslo")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4710">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_AGDER =
            Enhet(nummer = "4710", navn = "NAV Hjelpemiddelsentral Agder")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4712">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_VESTLAND_BERGEN =
            Enhet(nummer = "4712", navn = "NAV Hjelpemiddelsentral Vestland-Bergen")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/9999">NORG2</a>
         */
        val ANDRE_EKSTERNE =
            Enhet(nummer = "9999", navn = "ANDRE EKSTERNE")
    }
}
