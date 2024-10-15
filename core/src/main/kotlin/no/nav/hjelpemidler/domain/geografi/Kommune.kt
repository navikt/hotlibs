package no.nav.hjelpemidler.domain.geografi

import com.fasterxml.jackson.annotation.JsonAlias

data class Kommune(
    @JsonAlias("kommunenummer")
    override val nummer: String,
    @JsonAlias("kommunenavn")
    override val navn: String,
) : GeografiskOmråde() {
    /**
     * NB! Ikke alle kommuner er lagt til her pt. Kun de som brukes i logikk og/eller tester.
     */
    companion object {
        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/0301">DigiHoT Oppslag</a>
         */
        val OSLO =
            Kommune("0301", "Oslo")

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/4204">DigiHoT Oppslag</a>
         */
        val KRISTIANSAND =
            Kommune("4204", "Kristiansand")

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/4601">DigiHoT Oppslag</a>
         */
        val BERGEN =
            Kommune("4601", "Bergen")

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/5001">DigiHoT Oppslag</a>
         */
        val TRONDHEIM =
            Kommune("5001", "Trondheim")

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/5501">DigiHoT Oppslag</a>
         */
        val TROMSØ =
            Kommune("5501", "Tromsø")
    }
}
