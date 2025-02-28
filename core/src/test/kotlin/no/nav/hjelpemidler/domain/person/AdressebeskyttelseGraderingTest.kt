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

            gradering.strengtFortrolig shouldBe true
            gradering.fortrolig shouldBe false
            gradering.gradert shouldBe true
        }

        AdressebeskyttelseGradering.FORTROLIG.should { gradering ->
            gradering.kategori shouldBe Kategori.FORTROLIG

            gradering.strengtFortrolig shouldBe false
            gradering.fortrolig shouldBe true
            gradering.gradert shouldBe true
        }

        setOf(AdressebeskyttelseGradering.UGRADERT, null).forAll { gradering ->
            gradering.kategori shouldBe Kategori.UGRADERT

            gradering.strengtFortrolig shouldBe false
            gradering.fortrolig shouldBe false
            gradering.gradert shouldBe false
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

            gradering.kategori.strengtFortrolig shouldBe true
            gradering.kategori.fortrolig shouldBe false
            gradering.kategori.gradert shouldBe true

            gradering.gradert shouldBe true
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

            gradering.kategori.strengtFortrolig shouldBe true
            gradering.kategori.fortrolig shouldBe false
            gradering.kategori.gradert shouldBe true

            gradering.gradert shouldBe true
        }
    }

    @Test
    fun `FORTROLIG har presedens over UGRADERT`() {
        enumSetOf(
            AdressebeskyttelseGradering.UGRADERT,
            AdressebeskyttelseGradering.FORTROLIG,
        ).should { gradering ->
            gradering.kategori shouldBe Kategori.FORTROLIG

            gradering.kategori.strengtFortrolig shouldBe false
            gradering.kategori.fortrolig shouldBe true
            gradering.kategori.gradert shouldBe true

            gradering.gradert shouldBe true
        }
    }

    @Test
    fun `Tomt sett og sett av UGRADERT gir riktige resultater`() {
        setOf(emptyEnumSet(), enumSetOf(AdressebeskyttelseGradering.UGRADERT)).forAll { gradering ->
            gradering.kategori shouldBe Kategori.UGRADERT

            gradering.kategori.strengtFortrolig shouldBe false
            gradering.kategori.fortrolig shouldBe false
            gradering.kategori.gradert shouldBe false

            gradering.gradert shouldBe false
        }
    }
}
