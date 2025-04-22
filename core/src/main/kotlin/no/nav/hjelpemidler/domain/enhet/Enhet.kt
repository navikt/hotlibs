package no.nav.hjelpemidler.domain.enhet

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.hjelpemidler.validering.nummerValidator

open class Enhet(
    /**
     * Alias: "enhetNr" er bla. brukt i NORG2.
     */
    @JsonAlias("enhetsnummer", "enhetNr")
    final override val nummer: String,
    @JsonAlias("enhetsnavn")
    final override val navn: String,
) : AbstractEnhet() {
    @JsonIgnore
    constructor(enhet: Enhet) : this(enhet.nummer, enhet.navn)

    init {
        require(erGyldig(nummer)) { "Ugyldig enhetsnummer: '$nummer'" }
    }

    /**
     * NB! Dette er ikke en fullstendig liste av enheter. Kun de som brukes i logikk og/eller tester.
     *
     * @see [no.nav.hjelpemidler.domain.enhet.EnhetTest]
     */
    companion object {
        private val erGyldig = nummerValidator(lengde = 4)

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
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4700">NORG2</a>
         */
        val STYRINGSENHETEN_FOR_NAV_HJELPEMIDLER_OG_TILRETTELEGGING =
            Enhet(nummer = "4700", navn = "Styringsenheten for Nav hjelpemidler og tilrettelegging")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4701">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_ØST_VIKEN =
            Enhet(nummer = "4701", navn = "Nav hjelpemiddelsentral Øst-Viken")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4702">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_AKERSHUS =
            Enhet(nummer = "4702", navn = "Nav hjelpemiddelsentral Akershus")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4703">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_OSLO =
            Enhet(nummer = "4703", navn = "Nav hjelpemiddelsentral Oslo")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4704">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_INNLANDET_ELVERUM =
            Enhet(nummer = "4704", navn = "Nav hjelpemiddelsentral Innlandet-Elverum")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4705">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_INNLANDET_GJØVIK =
            Enhet(nummer = "4705", navn = "Nav hjelpemiddelsentral Innlandet-Gjøvik")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4706">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_VEST_VIKEN =
            Enhet(nummer = "4706", navn = "Nav hjelpemiddelsentral Vest-Viken")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4707">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_VESTFOLD_OG_TELEMARK =
            Enhet(nummer = "4707", navn = "Nav hjelpemiddelsentral Vestfold og Telemark")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4708">NORG2</a>
         */
        @Deprecated("Nedlagt")
        val NAV_HJELPEMIDDELSENTRAL_VESTFOLD_OG_TELEMARK_SKIEN =
            Enhet(nummer = "4708", navn = "NAV Hjelpemiddelsentral Vestfold og Telemark-Skien")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4709">NORG2</a>
         */
        @Deprecated("Nedlagt")
        val NAV_HJELPEMIDDELSENTRAL_AGDER_ARENDAL =
            Enhet(nummer = "4709", navn = "NAV Hjelpemiddelsentral Agder-Arendal")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4710">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_AGDER =
            Enhet(nummer = "4710", navn = "Nav hjelpemiddelsentral Agder")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4711">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_ROGALAND =
            Enhet(nummer = "4711", navn = "Nav hjelpemiddelsentral Rogaland")

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
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4718">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_NORDLAND =
            Enhet(nummer = "4718", navn = "Nav hjelpemiddelsentral Nordland")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4719">NORG2</a>
         */
        val NAV_HJELPEMIDDELSENTRAL_TROMS_OG_FINNMARK =
            Enhet(nummer = "4719", navn = "Nav hjelpemiddelsentral Troms og Finnmark")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/4720">NORG2</a>
         */
        @Deprecated("Under avvikling")
        val NAV_HJELPEMIDDELSENTRAL_TROMS_OG_FINNMARK_LAKSELV =
            Enhet(nummer = "4720", navn = "Nav hjelpemiddelsentral Troms og Finnmark-Lakselv")

        /**
         * @see <a href="https://norg2.intern.nav.no/norg2/api/v1/enhet/9999">NORG2</a>
         */
        val ANDRE_EKSTERNE =
            Enhet(nummer = "9999", navn = "Andre eksterne")
    }
}
