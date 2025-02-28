package no.nav.hjelpemidler.domain.person

import io.kotest.inspectors.forAll
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.collections.emptyEnumSet
import no.nav.hjelpemidler.collections.enumSetOf
import no.nav.hjelpemidler.domain.person.AdressebeskyttelseGradering.Kategori
import kotlin.test.Test

class AdressebeskyttelseGraderingTest {
    @Test
    fun `Gradering tilhører riktig kategori`() {
        setOf(
            AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND,
            AdressebeskyttelseGradering.STRENGT_FORTROLIG,
        ).forAll { gradering ->
            gradering.kategori shouldBe Kategori.STRENGT_FORTROLIG

            gradering.erStrengtFortrolig shouldBe true
            gradering.erFortrolig shouldBe false
            gradering.erGradert shouldBe true
        }

        AdressebeskyttelseGradering.FORTROLIG.should { gradering ->
            gradering.kategori shouldBe Kategori.FORTROLIG

            gradering.erStrengtFortrolig shouldBe false
            gradering.erFortrolig shouldBe true
            gradering.erGradert shouldBe true
        }

        setOf(AdressebeskyttelseGradering.UGRADERT, null).forAll { gradering ->
            gradering.kategori shouldBe Kategori.UGRADERT

            gradering.erStrengtFortrolig shouldBe false
            gradering.erFortrolig shouldBe false
            gradering.erGradert shouldBe false
        }
    }

    @Test
    fun `STRENGT_FORTROLIG_UTLAND har presedens over FORTROLIG`() {
        enumSetOf(
            AdressebeskyttelseGradering.UGRADERT,
            AdressebeskyttelseGradering.FORTROLIG,
            AdressebeskyttelseGradering.STRENGT_FORTROLIG,
            AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND,
        ).should { gradering ->
            gradering.kategori shouldBe Kategori.STRENGT_FORTROLIG

            gradering.kategori.erStrengtFortrolig shouldBe true
            gradering.kategori.erFortrolig shouldBe false
            gradering.kategori.erGradert shouldBe true

            gradering.erGradert shouldBe true
        }
    }

    @Test
    fun `STRENGT_FORTROLIG har presedens over FORTROLIG`() {
        enumSetOf(
            AdressebeskyttelseGradering.UGRADERT,
            AdressebeskyttelseGradering.FORTROLIG,
            AdressebeskyttelseGradering.STRENGT_FORTROLIG,
        ).should { gradering ->
            gradering.kategori shouldBe Kategori.STRENGT_FORTROLIG

            gradering.kategori.erStrengtFortrolig shouldBe true
            gradering.kategori.erFortrolig shouldBe false
            gradering.kategori.erGradert shouldBe true

            gradering.erGradert shouldBe true
        }
    }

    @Test
    fun `FORTROLIG har presedens over UGRADERT`() {
        enumSetOf(
            AdressebeskyttelseGradering.UGRADERT,
            AdressebeskyttelseGradering.FORTROLIG,
        ).should { gradering ->
            gradering.kategori shouldBe Kategori.FORTROLIG

            gradering.kategori.erStrengtFortrolig shouldBe false
            gradering.kategori.erFortrolig shouldBe true
            gradering.kategori.erGradert shouldBe true

            gradering.erGradert shouldBe true
        }
    }

    @Test
    fun `Tomt sett og sett av UGRADERT gir riktige resultater`() {
        setOf(emptyEnumSet(), enumSetOf(AdressebeskyttelseGradering.UGRADERT)).forAll { gradering ->
            gradering.kategori shouldBe Kategori.UGRADERT

            gradering.kategori.erStrengtFortrolig shouldBe false
            gradering.kategori.erFortrolig shouldBe false
            gradering.kategori.erGradert shouldBe false

            gradering.erGradert shouldBe false
        }
    }
}
