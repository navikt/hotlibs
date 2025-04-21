package no.nav.hjelpemidler.domain.geografi

import com.fasterxml.jackson.annotation.JsonAlias
import no.nav.hjelpemidler.validering.nummerValidator

open class Enhet(
    /**
     * Alias: "enhetNr" er bla. brukt i NORG2.
     */
    @JsonAlias("enhetsnummer", "enhetNr")
    val nummer: String,
    @JsonAlias("enhetsnavn")
    val navn: String,
) {
    init {
        require(validator(nummer)) { "Ugyldig enhetsnummer: '$nummer'" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Enhet
        return nummer == other.nummer
    }

    override fun hashCode(): Int = nummer.hashCode()

    override fun toString(): String = "$navn ($nummer)"

    /**
     * NB! Ikke alle sentraler er lagt til her pt. Kun de som brukes i logikk og/eller tester.
     *
     * @see [no.nav.hjelpemidler.domain.geografi.EnhetTest]
     */
    companion object {
        private val validator = nummerValidator(lengde = 4)

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/2103">NORG2</a>
         */
        val NAV_VIKAFOSSEN =
            Enhet(nummer = "2103", navn = "Nav Vikafossen")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/2970">NORG2</a>
         */
        val IT_AVDELINGEN =
            Enhet(nummer = "2970", navn = "IT-avdelingen")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4303">NORG2</a>
         */
        val NAV_ID_OG_FORDELING =
            Enhet(nummer = "4303", navn = "Nav id og fordeling")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4485">NORG2</a>
         */
        val AY_HJELPEMIDLER =
            Enhet(nummer = "4485", navn = "Nav arbeid og ytelser - hjelpemidler")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4701">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_ØST_VIKEN =
            Enhet(nummer = "4701", navn = "Nav hjelpemiddelsentral Øst-Viken")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4703">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_OSLO =
            Enhet(nummer = "4703", navn = "Nav hjelpemiddelsentral Oslo")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4707">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_VESTFOLD_OG_TELEMARK =
            Enhet(nummer = "4707", navn = "Nav hjelpemiddelsentral Vestfold og Telemark")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4710">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_AGDER =
            Enhet(nummer = "4710", navn = "Nav hjelpemiddelsentral Agder")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4712">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_VESTLAND_BERGEN =
            Enhet(nummer = "4712", navn = "Nav hjelpemiddelsentral Vestland-Bergen")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4714">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_VESTLAND_FØRDE =
            Enhet(nummer = "4714", navn = "Nav hjelpemiddelsentral Vestland-Førde")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4715">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_MØRE_OG_ROMSDAL =
            Enhet(nummer = "4715", navn = "Nav hjelpemiddelsentral Møre og Romsdal")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4716">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_TRØNDELAG =
            Enhet(nummer = "4716", navn = "Nav hjelpemiddelsentral Trøndelag")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/9999">NORG2</a>
         */
        val ANDRE_EKSTERNE =
            Enhet(nummer = "9999", navn = "Andre eksterne")
    }
}
