package no.nav.hjelpemidler.nare.policy

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.nare.policy.Policyavgjørelse.IKKE_AKTUELT
import no.nav.hjelpemidler.nare.policy.Policyavgjørelse.NEKT
import no.nav.hjelpemidler.nare.policy.Policyavgjørelse.TILLAT
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream
import kotlin.reflect.KFunction2
import kotlin.test.Test

class PolicyavgjørelseTest {
    @TestFactory
    fun og(): Stream<DynamicTest> = test(
        Case(x = TILLAT, operator = Policyavgjørelse::og, y = TILLAT, expected = TILLAT),
        Case(x = TILLAT, operator = Policyavgjørelse::og, y = NEKT, expected = NEKT),
        Case(x = TILLAT, operator = Policyavgjørelse::og, y = IKKE_AKTUELT, expected = IKKE_AKTUELT),
        Case(x = NEKT, operator = Policyavgjørelse::og, y = TILLAT, expected = NEKT),
        Case(x = NEKT, operator = Policyavgjørelse::og, y = NEKT, expected = NEKT),
        Case(x = NEKT, operator = Policyavgjørelse::og, y = IKKE_AKTUELT, expected = NEKT),
        Case(x = IKKE_AKTUELT, operator = Policyavgjørelse::og, y = TILLAT, expected = IKKE_AKTUELT),
        Case(x = IKKE_AKTUELT, operator = Policyavgjørelse::og, y = NEKT, expected = NEKT),
        Case(x = IKKE_AKTUELT, operator = Policyavgjørelse::og, y = IKKE_AKTUELT, expected = IKKE_AKTUELT),
    )

    @TestFactory
    fun eller(): Stream<DynamicTest> = test(
        Case(x = TILLAT, operator = Policyavgjørelse::eller, y = TILLAT, expected = TILLAT),
        Case(x = TILLAT, operator = Policyavgjørelse::eller, y = NEKT, expected = TILLAT),
        Case(x = TILLAT, operator = Policyavgjørelse::eller, y = IKKE_AKTUELT, expected = TILLAT),
        Case(x = NEKT, operator = Policyavgjørelse::eller, y = TILLAT, expected = TILLAT),
        Case(x = NEKT, operator = Policyavgjørelse::eller, y = NEKT, expected = NEKT),
        Case(x = NEKT, operator = Policyavgjørelse::eller, y = IKKE_AKTUELT, expected = IKKE_AKTUELT),
        Case(x = IKKE_AKTUELT, operator = Policyavgjørelse::eller, y = TILLAT, expected = TILLAT),
        Case(x = IKKE_AKTUELT, operator = Policyavgjørelse::eller, y = NEKT, expected = IKKE_AKTUELT),
        Case(x = IKKE_AKTUELT, operator = Policyavgjørelse::eller, y = IKKE_AKTUELT, expected = IKKE_AKTUELT),
    )

    @Test
    fun ikke() {
        TILLAT.ikke() shouldBe NEKT
        NEKT.ikke() shouldBe TILLAT
        IKKE_AKTUELT.ikke() shouldBe IKKE_AKTUELT
    }

    private fun test(vararg cases: Case): Stream<DynamicTest> = DynamicTest.stream(cases.iterator(), Case::toString) {
        it.operator(it.x, it.y) shouldBe it.expected
    }

    class Case(
        val x: Policyavgjørelse,
        val operator: KFunction2<Policyavgjørelse, Policyavgjørelse, Policyavgjørelse>,
        val y: Policyavgjørelse,
        val expected: Policyavgjørelse,
    ) {
        override fun toString(): String = "$x ${operator.name} $y = $expected"
    }
}
