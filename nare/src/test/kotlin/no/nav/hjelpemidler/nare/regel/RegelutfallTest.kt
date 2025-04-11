package no.nav.hjelpemidler.nare.regel

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.nare.regel.Regelutfall.JA
import no.nav.hjelpemidler.nare.regel.Regelutfall.KANSKJE
import no.nav.hjelpemidler.nare.regel.Regelutfall.NEI
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream
import kotlin.reflect.KFunction2
import kotlin.test.Test

class RegelutfallTest {
    @TestFactory
    fun og(): Stream<DynamicTest> = test(
        Case(x = JA, operator = Regelutfall::og, y = JA, expected = JA),
        Case(x = JA, operator = Regelutfall::og, y = NEI, expected = NEI),
        Case(x = JA, operator = Regelutfall::og, y = KANSKJE, expected = KANSKJE),
        Case(x = NEI, operator = Regelutfall::og, y = JA, expected = NEI),
        Case(x = NEI, operator = Regelutfall::og, y = NEI, expected = NEI),
        Case(x = NEI, operator = Regelutfall::og, y = KANSKJE, expected = NEI),
        Case(x = KANSKJE, operator = Regelutfall::og, y = JA, expected = KANSKJE),
        Case(x = KANSKJE, operator = Regelutfall::og, y = NEI, expected = NEI),
        Case(x = KANSKJE, operator = Regelutfall::og, y = KANSKJE, expected = KANSKJE),
    )

    @TestFactory
    fun eller(): Stream<DynamicTest> = test(
        Case(x = JA, operator = Regelutfall::eller, y = JA, expected = JA),
        Case(x = JA, operator = Regelutfall::eller, y = NEI, expected = JA),
        Case(x = JA, operator = Regelutfall::eller, y = KANSKJE, expected = JA),
        Case(x = NEI, operator = Regelutfall::eller, y = JA, expected = JA),
        Case(x = NEI, operator = Regelutfall::eller, y = NEI, expected = NEI),
        Case(x = NEI, operator = Regelutfall::eller, y = KANSKJE, expected = KANSKJE),
        Case(x = KANSKJE, operator = Regelutfall::eller, y = JA, expected = JA),
        Case(x = KANSKJE, operator = Regelutfall::eller, y = NEI, expected = KANSKJE),
        Case(x = KANSKJE, operator = Regelutfall::eller, y = KANSKJE, expected = KANSKJE),
    )

    @Test
    fun ikke() {
        JA.ikke() shouldBe NEI
        NEI.ikke() shouldBe JA
        KANSKJE.ikke() shouldBe KANSKJE
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
