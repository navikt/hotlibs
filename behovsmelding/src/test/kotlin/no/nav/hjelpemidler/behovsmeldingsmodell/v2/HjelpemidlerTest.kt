package no.nav.hjelpemidler.behovsmeldingsmodell.v2

import io.kotest.matchers.collections.shouldBeSameSizeAs
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import kotlin.test.Test

class HjelpemidlerTest {
    @Test
    fun `Skal lage liste av alle artikler i behovsmelding`() {
        val hjelpemidler = (1..2).map { hIndex ->
            val tilbehør = (1..3).map { tIndex -> lagTilbehør("HT$hIndex$tIndex") }
            lagHjelpemiddel("H$hIndex", tilbehør = tilbehør)
        }
        Hjelpemidler(
            hjelpemidler = hjelpemidler,
            tilbehør = (1..2).map { lagTilbehør("T$it") },
            totaltAntall = 0
        ).should {
            it.hmsArtNrs shouldHaveSize 2 + 2 * 3 + 2

            val artikler = it.artikler
            val expected = listOf(
                it.hjelpemidler[0],
                it.hjelpemidler[0].tilbehør[0],
                it.hjelpemidler[0].tilbehør[1],
                it.hjelpemidler[0].tilbehør[2],
                it.hjelpemidler[1],
                it.hjelpemidler[1].tilbehør[0],
                it.hjelpemidler[1].tilbehør[1],
                it.hjelpemidler[1].tilbehør[2],
                it.tilbehør[0],
                it.tilbehør[1],
            )
            artikler shouldBeSameSizeAs expected
            artikler shouldContainInOrder expected
        }
    }
}
