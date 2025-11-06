package no.nav.hjelpemidler.behovsmeldingsmodell.v2

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class HjelpemidlerTest {
    @Test
    fun `Skal lage liste av alle artikler i behovsmelding`() {
        val hjelpemidler = lagHjelpemidler()
        hjelpemidler.totaltAntall shouldBe 11
        hjelpemidler.hmsArtNrs shouldHaveSize 6
        hjelpemidler.artikler shouldHaveSize 8
    }
}
