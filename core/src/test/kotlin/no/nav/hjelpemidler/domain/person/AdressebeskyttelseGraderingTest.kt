package no.nav.hjelpemidler.domain.person

import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class AdressebeskyttelseGraderingTest {
    @Test
    fun `STRENGT_FORTROLIG_UTLAND har presedens over STRENGT_FORTROLIG`() {
        setOf(
            AdressebeskyttelseGradering.UGRADERT,
            AdressebeskyttelseGradering.FORTROLIG,
            AdressebeskyttelseGradering.STRENGT_FORTROLIG,
            AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND,
        ).should {
            it.erStrengtFortrolig shouldBe true
            it.erUgradert shouldBe false
        }
    }

    @Test
    fun `STRENGT_FORTROLIG har presedens over FORTROLIG`() {
        setOf(
            AdressebeskyttelseGradering.UGRADERT,
            AdressebeskyttelseGradering.FORTROLIG,
            AdressebeskyttelseGradering.STRENGT_FORTROLIG,
        ).should {
            it.erStrengtFortrolig shouldBe true
            it.erUgradert shouldBe false
        }
    }

    @Test
    fun `FORTROLIG har presedens over UGRADERT`() {
        setOf(
            AdressebeskyttelseGradering.UGRADERT,
            AdressebeskyttelseGradering.FORTROLIG,
        ).should {
            it.erFortrolig shouldBe true
            it.erUgradert shouldBe false
        }
    }
}
