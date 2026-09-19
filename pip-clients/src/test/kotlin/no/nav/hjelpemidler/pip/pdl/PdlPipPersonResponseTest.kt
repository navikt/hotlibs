package no.nav.hjelpemidler.pip.pdl

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.hjelpemidler.domain.geografi.GeografiskOmråde
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import org.junit.jupiter.api.Test

class PdlPipPersonResponseTest {
    @Test
    fun `geografiskOmråde er kommune`() {
        val fnr = Fødselsnummer(50.år)
        val response = lagPipPersonResponse(
            fnr, geografiskTilknytning = PdlPipGeografiskTilknytning(
                type = PdlPipGeografiskTilknytning.Type.KOMMUNE,
                kommune = "0301",
                bydel = "030105",
                land = null,
                regel = null,
            )
        )
        val kommune = response.geografiskOmråde.shouldBeInstanceOf<GeografiskOmråde.Kommune>()
        kommune.id shouldBe response.geografiskTilknytning.kommune
    }

    @Test
    fun `geografiskOmråde er bydel`() {
        val fnr = Fødselsnummer(40.år)
        val response = lagPipPersonResponse(
            fnr, geografiskTilknytning = PdlPipGeografiskTilknytning(
                type = PdlPipGeografiskTilknytning.Type.BYDEL,
                kommune = "0301",
                bydel = "030105",
                land = null,
                regel = null,
            )
        )
        val bydel = response.geografiskOmråde.shouldBeInstanceOf<GeografiskOmråde.Bydel>()
        bydel.id shouldBe response.geografiskTilknytning.bydel
    }

    @Test
    fun `geografiskOmråde er land`() {
        val fnr = Fødselsnummer(30.år)
        val response = lagPipPersonResponse(
            fnr, geografiskTilknytning = PdlPipGeografiskTilknytning(
                type = PdlPipGeografiskTilknytning.Type.UTLAND,
                kommune = null,
                bydel = null,
                land = "SWE",
                regel = null,
            )
        )
        val land = response.geografiskOmråde.shouldBeInstanceOf<GeografiskOmråde.Land>()
        land.id shouldBe response.geografiskTilknytning.land
    }
}
