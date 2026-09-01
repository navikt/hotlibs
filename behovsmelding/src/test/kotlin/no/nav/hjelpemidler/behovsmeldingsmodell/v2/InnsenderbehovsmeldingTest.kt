package no.nav.hjelpemidler.behovsmeldingsmodell.v2

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.should
import kotlin.test.Test

class InnsenderbehovsmeldingTest {
    @Test
    fun `alleVedlegg skal inneholde vedlegg fra toppnivå, produktkategori og komponenter på produktkategori`() {
        val vedleggPåToppnivå = lagVedlegg(VedleggType.LEGEERKLÆRING_FOR_VARMEHJELPEMIDDEL)
        val vedleggPåKategori = lagVedlegg(VedleggType.DØRAUTOMATIKK_MÅLSATT_TEGNING)
        val vedleggPåKomponent = lagVedlegg(VedleggType.DØRAUTOMATIKK_DØR_BILDE)

        val produktkategori = lagProduktkategori(
            vedlegg = listOf(vedleggPåKategori),
            komponenter = listOf(lagProduktkategoriKomponent(vedlegg = listOf(vedleggPåKomponent))),
        )
        val behovsmelding = lagInnsenderbehovsmelding(
            hjelpemidler = Hjelpemidler(
                hjelpemidler = emptyList(),
                produktkategorier = listOf(produktkategori),
                totaltAntall = 0,
            ),
            vedlegg = listOf(vedleggPåToppnivå),
        )

        behovsmelding.alleVedlegg.should {
            it shouldContainExactlyInAnyOrder listOf(vedleggPåToppnivå, vedleggPåKategori, vedleggPåKomponent)
        }
    }

    @Test
    fun `alleVedlegg skal være tom liste når det ikke finnes vedlegg noe sted`() {
        val behovsmelding = lagInnsenderbehovsmelding()

        behovsmelding.alleVedlegg.should { it shouldContainExactlyInAnyOrder emptyList() }
    }
}
