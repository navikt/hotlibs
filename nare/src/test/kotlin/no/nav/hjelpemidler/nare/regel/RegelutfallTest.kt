package no.nav.hjelpemidler.nare.regel

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream
import kotlin.reflect.KFunction2
import kotlin.test.Test

class RegelutfallTest {
    @TestFactory
    fun og(): Stream<DynamicTest> = test(
        Case(x = Regelutfall.JA, operator = Regelutfall::og, y = Regelutfall.JA, expected = Regelutfall.JA),
        Case(x = Regelutfall.JA, operator = Regelutfall::og, y = Regelutfall.NEI, expected = Regelutfall.NEI),
        Case(x = Regelutfall.JA, operator = Regelutfall::og, y = Regelutfall.KANSKJE, expected = Regelutfall.KANSKJE),
        Case(x = Regelutfall.NEI, operator = Regelutfall::og, y = Regelutfall.JA, expected = Regelutfall.NEI),
        Case(x = Regelutfall.NEI, operator = Regelutfall::og, y = Regelutfall.NEI, expected = Regelutfall.NEI),
        Case(x = Regelutfall.NEI, operator = Regelutfall::og, y = Regelutfall.KANSKJE, expected = Regelutfall.NEI),
        Case(x = Regelutfall.KANSKJE, operator = Regelutfall::og, y = Regelutfall.JA, expected = Regelutfall.KANSKJE),
        Case(x = Regelutfall.KANSKJE, operator = Regelutfall::og, y = Regelutfall.NEI, expected = Regelutfall.NEI),
        Case(
            x = Regelutfall.KANSKJE,
            operator = Regelutfall::og,
            y = Regelutfall.KANSKJE,
            expected = Regelutfall.KANSKJE
        ),
    )

    @TestFactory
    fun eller(): Stream<DynamicTest> = test(
        Case(x = Regelutfall.JA, operator = Regelutfall::eller, y = Regelutfall.JA, expected = Regelutfall.JA),
        Case(x = Regelutfall.JA, operator = Regelutfall::eller, y = Regelutfall.NEI, expected = Regelutfall.JA),
        Case(x = Regelutfall.JA, operator = Regelutfall::eller, y = Regelutfall.KANSKJE, expected = Regelutfall.JA),
        Case(x = Regelutfall.NEI, operator = Regelutfall::eller, y = Regelutfall.JA, expected = Regelutfall.JA),
        Case(x = Regelutfall.NEI, operator = Regelutfall::eller, y = Regelutfall.NEI, expected = Regelutfall.NEI),
        Case(
            x = Regelutfall.NEI,
            operator = Regelutfall::eller,
            y = Regelutfall.KANSKJE,
            expected = Regelutfall.KANSKJE
        ),
        Case(x = Regelutfall.KANSKJE, operator = Regelutfall::eller, y = Regelutfall.JA, expected = Regelutfall.JA),
        Case(
            x = Regelutfall.KANSKJE,
            operator = Regelutfall::eller,
            y = Regelutfall.NEI,
            expected = Regelutfall.KANSKJE
        ),
        Case(
            x = Regelutfall.KANSKJE,
            operator = Regelutfall::eller,
            y = Regelutfall.KANSKJE,
            expected = Regelutfall.KANSKJE
        ),
    )

    @Test
    fun ikke() {
        Regelutfall.JA.ikke() shouldBe Regelutfall.NEI
        Regelutfall.NEI.ikke() shouldBe Regelutfall.JA
        Regelutfall.KANSKJE.ikke() shouldBe Regelutfall.KANSKJE
    }

    private fun test(vararg cases: Case): Stream<DynamicTest> = DynamicTest.stream(cases.iterator(), Case::toString) {
        it.operator(it.x, it.y) shouldBe it.expected
    }

    class Case(
        val x: Regelutfall,
        val operator: KFunction2<Regelutfall, Regelutfall, Regelutfall>,
        val y: Regelutfall,
        val expected: Regelutfall,
    ) {
        override fun toString(): String = "$x ${operator.name} $y = $expected"
    }
}
