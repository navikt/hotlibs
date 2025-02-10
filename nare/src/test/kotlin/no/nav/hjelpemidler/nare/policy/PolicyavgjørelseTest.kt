package no.nav.hjelpemidler.nare.policy

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream
import kotlin.reflect.KFunction2
import kotlin.test.Test

class PolicyavgjørelseTest {
    @TestFactory
    fun og(): Stream<DynamicTest> = test(
        Case(
            x = Policyavgjørelse.TILLAT,
            operator = Policyavgjørelse::og,
            y = Policyavgjørelse.TILLAT,
            expected = Policyavgjørelse.TILLAT
        ),
        Case(
            x = Policyavgjørelse.TILLAT,
            operator = Policyavgjørelse::og,
            y = Policyavgjørelse.NEKT,
            expected = Policyavgjørelse.NEKT
        ),
        Case(
            x = Policyavgjørelse.TILLAT,
            operator = Policyavgjørelse::og,
            y = Policyavgjørelse.IKKE_AKTUELT,
            expected = Policyavgjørelse.IKKE_AKTUELT
        ),
        Case(
            x = Policyavgjørelse.NEKT,
            operator = Policyavgjørelse::og,
            y = Policyavgjørelse.TILLAT,
            expected = Policyavgjørelse.NEKT
        ),
        Case(
            x = Policyavgjørelse.NEKT,
            operator = Policyavgjørelse::og,
            y = Policyavgjørelse.NEKT,
            expected = Policyavgjørelse.NEKT
        ),
        Case(
            x = Policyavgjørelse.NEKT,
            operator = Policyavgjørelse::og,
            y = Policyavgjørelse.IKKE_AKTUELT,
            expected = Policyavgjørelse.NEKT
        ),
        Case(
            x = Policyavgjørelse.IKKE_AKTUELT,
            operator = Policyavgjørelse::og,
            y = Policyavgjørelse.TILLAT,
            expected = Policyavgjørelse.IKKE_AKTUELT
        ),
        Case(
            x = Policyavgjørelse.IKKE_AKTUELT,
            operator = Policyavgjørelse::og,
            y = Policyavgjørelse.NEKT,
            expected = Policyavgjørelse.NEKT
        ),
        Case(
            x = Policyavgjørelse.IKKE_AKTUELT,
            operator = Policyavgjørelse::og,
            y = Policyavgjørelse.IKKE_AKTUELT,
            expected = Policyavgjørelse.IKKE_AKTUELT
        ),
    )

    @TestFactory
    fun eller(): Stream<DynamicTest> = test(
        Case(
            x = Policyavgjørelse.TILLAT,
            operator = Policyavgjørelse::eller,
            y = Policyavgjørelse.TILLAT,
            expected = Policyavgjørelse.TILLAT
        ),
        Case(
            x = Policyavgjørelse.TILLAT,
            operator = Policyavgjørelse::eller,
            y = Policyavgjørelse.NEKT,
            expected = Policyavgjørelse.TILLAT
        ),
        Case(
            x = Policyavgjørelse.TILLAT,
            operator = Policyavgjørelse::eller,
            y = Policyavgjørelse.IKKE_AKTUELT,
            expected = Policyavgjørelse.TILLAT
        ),
        Case(
            x = Policyavgjørelse.NEKT,
            operator = Policyavgjørelse::eller,
            y = Policyavgjørelse.TILLAT,
            expected = Policyavgjørelse.TILLAT
        ),
        Case(
            x = Policyavgjørelse.NEKT,
            operator = Policyavgjørelse::eller,
            y = Policyavgjørelse.NEKT,
            expected = Policyavgjørelse.NEKT
        ),
        Case(
            x = Policyavgjørelse.NEKT,
            operator = Policyavgjørelse::eller,
            y = Policyavgjørelse.IKKE_AKTUELT,
            expected = Policyavgjørelse.IKKE_AKTUELT
        ),
        Case(
            x = Policyavgjørelse.IKKE_AKTUELT,
            operator = Policyavgjørelse::eller,
            y = Policyavgjørelse.TILLAT,
            expected = Policyavgjørelse.TILLAT
        ),
        Case(
            x = Policyavgjørelse.IKKE_AKTUELT,
            operator = Policyavgjørelse::eller,
            y = Policyavgjørelse.NEKT,
            expected = Policyavgjørelse.IKKE_AKTUELT
        ),
        Case(
            x = Policyavgjørelse.IKKE_AKTUELT,
            operator = Policyavgjørelse::eller,
            y = Policyavgjørelse.IKKE_AKTUELT,
            expected = Policyavgjørelse.IKKE_AKTUELT
        ),
    )

    @Test
    fun ikke() {
        Policyavgjørelse.TILLAT.ikke() shouldBe Policyavgjørelse.NEKT
        Policyavgjørelse.NEKT.ikke() shouldBe Policyavgjørelse.TILLAT
        Policyavgjørelse.IKKE_AKTUELT.ikke() shouldBe Policyavgjørelse.IKKE_AKTUELT
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
