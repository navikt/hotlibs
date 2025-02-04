package no.nav.hjelpemidler.nare.policy

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream
import kotlin.reflect.KFunction2
import kotlin.test.Test

class PolicyResultatTest {
    @TestFactory
    fun og(): Stream<DynamicTest> = test(
        Case(
            x = PolicyResultat.TILLAT,
            operator = PolicyResultat::og,
            y = PolicyResultat.TILLAT,
            expected = PolicyResultat.TILLAT
        ),
        Case(
            x = PolicyResultat.TILLAT,
            operator = PolicyResultat::og,
            y = PolicyResultat.NEKT,
            expected = PolicyResultat.NEKT
        ),
        Case(
            x = PolicyResultat.TILLAT,
            operator = PolicyResultat::og,
            y = PolicyResultat.IKKE_AKTUELT,
            expected = PolicyResultat.IKKE_AKTUELT
        ),
        Case(
            x = PolicyResultat.NEKT,
            operator = PolicyResultat::og,
            y = PolicyResultat.TILLAT,
            expected = PolicyResultat.NEKT
        ),
        Case(
            x = PolicyResultat.NEKT,
            operator = PolicyResultat::og,
            y = PolicyResultat.NEKT,
            expected = PolicyResultat.NEKT
        ),
        Case(
            x = PolicyResultat.NEKT,
            operator = PolicyResultat::og,
            y = PolicyResultat.IKKE_AKTUELT,
            expected = PolicyResultat.NEKT
        ),
        Case(
            x = PolicyResultat.IKKE_AKTUELT,
            operator = PolicyResultat::og,
            y = PolicyResultat.TILLAT,
            expected = PolicyResultat.IKKE_AKTUELT
        ),
        Case(
            x = PolicyResultat.IKKE_AKTUELT,
            operator = PolicyResultat::og,
            y = PolicyResultat.NEKT,
            expected = PolicyResultat.NEKT
        ),
        Case(
            x = PolicyResultat.IKKE_AKTUELT,
            operator = PolicyResultat::og,
            y = PolicyResultat.IKKE_AKTUELT,
            expected = PolicyResultat.IKKE_AKTUELT
        ),
    )

    @TestFactory
    fun eller(): Stream<DynamicTest> = test(
        Case(
            x = PolicyResultat.TILLAT,
            operator = PolicyResultat::eller,
            y = PolicyResultat.TILLAT,
            expected = PolicyResultat.TILLAT
        ),
        Case(
            x = PolicyResultat.TILLAT,
            operator = PolicyResultat::eller,
            y = PolicyResultat.NEKT,
            expected = PolicyResultat.TILLAT
        ),
        Case(
            x = PolicyResultat.TILLAT,
            operator = PolicyResultat::eller,
            y = PolicyResultat.IKKE_AKTUELT,
            expected = PolicyResultat.TILLAT
        ),
        Case(
            x = PolicyResultat.NEKT,
            operator = PolicyResultat::eller,
            y = PolicyResultat.TILLAT,
            expected = PolicyResultat.TILLAT
        ),
        Case(
            x = PolicyResultat.NEKT,
            operator = PolicyResultat::eller,
            y = PolicyResultat.NEKT,
            expected = PolicyResultat.NEKT
        ),
        Case(
            x = PolicyResultat.NEKT,
            operator = PolicyResultat::eller,
            y = PolicyResultat.IKKE_AKTUELT,
            expected = PolicyResultat.IKKE_AKTUELT
        ),
        Case(
            x = PolicyResultat.IKKE_AKTUELT,
            operator = PolicyResultat::eller,
            y = PolicyResultat.TILLAT,
            expected = PolicyResultat.TILLAT
        ),
        Case(
            x = PolicyResultat.IKKE_AKTUELT,
            operator = PolicyResultat::eller,
            y = PolicyResultat.NEKT,
            expected = PolicyResultat.IKKE_AKTUELT
        ),
        Case(
            x = PolicyResultat.IKKE_AKTUELT,
            operator = PolicyResultat::eller,
            y = PolicyResultat.IKKE_AKTUELT,
            expected = PolicyResultat.IKKE_AKTUELT
        ),
    )

    @Test
    fun ikke() {
        PolicyResultat.TILLAT.ikke() shouldBe PolicyResultat.NEKT
        PolicyResultat.NEKT.ikke() shouldBe PolicyResultat.TILLAT
        PolicyResultat.IKKE_AKTUELT.ikke() shouldBe PolicyResultat.IKKE_AKTUELT
    }

    private fun test(vararg cases: Case): Stream<DynamicTest> = DynamicTest.stream(cases.iterator(), Case::toString) {
        it.operator(it.x, it.y) shouldBe it.expected
    }

    class Case(
        val x: PolicyResultat,
        val operator: KFunction2<PolicyResultat, PolicyResultat, PolicyResultat>,
        val y: PolicyResultat,
        val expected: PolicyResultat,
    ) {
        override fun toString(): String = "$x ${operator.name} $y = $expected"
    }
}
