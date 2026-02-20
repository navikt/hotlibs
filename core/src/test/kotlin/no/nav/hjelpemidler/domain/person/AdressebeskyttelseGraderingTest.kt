package no.nav.hjelpemidler.domain.person

import io.kotest.inspectors.forAll
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.collections.emptyEnumSet
import no.nav.hjelpemidler.collections.enumSetOf
import kotlin.test.Test

class AdressebeskyttelseGraderingTest {
    @Test
    fun `STRENGT_FORTROLIG_UTLAND er strengeste gradering`() {
        enumSetOf(
            AdressebeskyttelseGradering.UGRADERT,
            AdressebeskyttelseGradering.FORTROLIG,
            AdressebeskyttelseGradering.STRENGT_FORTROLIG,
            AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND,
        ).strengeste.should { gradering ->
            gradering shouldBe AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND
        }
    }

    @Test
    fun `STRENGT_FORTROLIG_UTLAND har presedens over UGRADERT`() {
        enumSetOf(
            AdressebeskyttelseGradering.UGRADERT,
            AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND,
        ).strengeste.should { gradering ->
            gradering shouldBe AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND
        }
    }

    @Test
    fun `STRENGT_FORTROLIG er strengeste gradering`() {
        enumSetOf(
            AdressebeskyttelseGradering.UGRADERT,
            AdressebeskyttelseGradering.FORTROLIG,
            AdressebeskyttelseGradering.STRENGT_FORTROLIG,
        ).strengeste.should { gradering ->
            gradering shouldBe AdressebeskyttelseGradering.STRENGT_FORTROLIG
        }
    }

    @Test
    fun `STRENGT_FORTROLIG har presedens over UGRADERT`() {
        enumSetOf(
            AdressebeskyttelseGradering.UGRADERT,
            AdressebeskyttelseGradering.STRENGT_FORTROLIG,
        ).strengeste.should { gradering ->
            gradering shouldBe AdressebeskyttelseGradering.STRENGT_FORTROLIG
        }
    }

    @Test
    fun `FORTROLIG er strengeste gradering`() {
        enumSetOf(
            AdressebeskyttelseGradering.UGRADERT,
            AdressebeskyttelseGradering.FORTROLIG,
        ).strengeste.should { gradering ->
            gradering shouldBe AdressebeskyttelseGradering.FORTROLIG
        }
    }

    @Test
    fun `Tomt sett og sett av UGRADERT gir UGRADERT`() {
        setOf(emptyEnumSet(), enumSetOf(AdressebeskyttelseGradering.UGRADERT))
            .map(Set<AdressebeskyttelseGradering>::strengeste)
            .forAll { gradering ->
                gradering shouldBe AdressebeskyttelseGradering.UGRADERT
            }
    }

    @Test
    fun `Boolske felter gir riktig resultat`() {
        enumSetOf(
            AdressebeskyttelseGradering.STRENGT_FORTROLIG,
            AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND,
        ).forAll { gradering ->
            gradering.isStrengtFortrolig shouldBe true
            gradering.isFortrolig shouldBe false
            gradering.isGradert shouldBe true
        }

        enumSetOf(
            AdressebeskyttelseGradering.FORTROLIG,
        ).forAll { gradering ->
            gradering.isStrengtFortrolig shouldBe false
            gradering.isFortrolig shouldBe true
            gradering.isGradert shouldBe true
        }

        enumSetOf(
            AdressebeskyttelseGradering.UGRADERT,
        ).forAll { gradering ->
            gradering.isStrengtFortrolig shouldBe false
            gradering.isFortrolig shouldBe false
            gradering.isGradert shouldBe false
        }
    }
}
