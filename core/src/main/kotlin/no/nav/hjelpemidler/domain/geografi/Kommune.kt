package no.nav.hjelpemidler.domain.geografi

import com.fasterxml.jackson.annotation.JsonAlias
import no.nav.hjelpemidler.validering.nummerValidator

class Kommune(
    @JsonAlias("kommunenummer")
    override val nummer: String,
    @JsonAlias("kommunenavn")
    override val navn: String,
) : GeografiskOmråde() {
    init {
        require(validator(nummer)) { "Ugyldig kommunenummer: '$nummer'" }
    }

    /**
     * NB! Ikke alle kommuner er lagt til her pt. Kun de som brukes i logikk og/eller tester.
     */
    companion object {
        private val validator = nummerValidator(lengde = 4)

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/0301">DigiHoT Oppslag</a>
         */
        val OSLO = Kommune("0301", "Oslo")

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/1508">DigiHoT Oppslag</a>
         */
        val ÅLESUND = Kommune("1508", "Ålesund")

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/3120">DigiHoT Oppslag</a>
         */
        val RAKKESTAD = Kommune("3120", "Rakkestad")

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/3201">DigiHoT Oppslag</a>
         */
        val BÆRUM = Kommune("3201", "Bærum")

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/3203">DigiHoT Oppslag</a>
         */
        val ASKER = Kommune("3203", "Asker")

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/3907">DigiHoT Oppslag</a>
         */
        val SANDEFJORD = Kommune("3907", "Sandefjord")

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/4204">DigiHoT Oppslag</a>
         */
        val KRISTIANSAND = Kommune("4204", "Kristiansand")

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/4601">DigiHoT Oppslag</a>
         */
        val BERGEN = Kommune("4601", "Bergen")

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/5001">DigiHoT Oppslag</a>
         */
        val TRONDHEIM = Kommune("5001", "Trondheim")

        /**
         * @see <a href="https://digihot-oppslag.intern.dev.nav.no/api/geografi/kommuner/5501">DigiHoT Oppslag</a>
         */
        val TROMSØ = Kommune("5501", "Tromsø")
    }
}
