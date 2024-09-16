package no.nav.hjelpemidler.nare.evaluering

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream
import kotlin.reflect.KFunction2
import kotlin.test.Test

class ResultatTest {
    @TestFactory
    fun og(): Stream<DynamicTest> = test(
        Case(x = Resultat.JA, operator = Resultat::og, y = Resultat.JA, expected = Resultat.JA),
        Case(x = Resultat.JA, operator = Resultat::og, y = Resultat.NEI, expected = Resultat.NEI),
        Case(x = Resultat.JA, operator = Resultat::og, y = Resultat.KANSKJE, expected = Resultat.KANSKJE),
        Case(x = Resultat.NEI, operator = Resultat::og, y = Resultat.JA, expected = Resultat.NEI),
        Case(x = Resultat.NEI, operator = Resultat::og, y = Resultat.NEI, expected = Resultat.NEI),
        Case(x = Resultat.NEI, operator = Resultat::og, y = Resultat.KANSKJE, expected = Resultat.NEI),
        Case(x = Resultat.KANSKJE, operator = Resultat::og, y = Resultat.JA, expected = Resultat.KANSKJE),
        Case(x = Resultat.KANSKJE, operator = Resultat::og, y = Resultat.NEI, expected = Resultat.NEI),
        Case(x = Resultat.KANSKJE, operator = Resultat::og, y = Resultat.KANSKJE, expected = Resultat.KANSKJE),
    )

    @TestFactory
    fun eller(): Stream<DynamicTest> = test(
        Case(x = Resultat.JA, operator = Resultat::eller, y = Resultat.JA, expected = Resultat.JA),
        Case(x = Resultat.JA, operator = Resultat::eller, y = Resultat.NEI, expected = Resultat.JA),
        Case(x = Resultat.JA, operator = Resultat::eller, y = Resultat.KANSKJE, expected = Resultat.JA),
        Case(x = Resultat.NEI, operator = Resultat::eller, y = Resultat.JA, expected = Resultat.JA),
        Case(x = Resultat.NEI, operator = Resultat::eller, y = Resultat.NEI, expected = Resultat.NEI),
        Case(x = Resultat.NEI, operator = Resultat::eller, y = Resultat.KANSKJE, expected = Resultat.KANSKJE),
        Case(x = Resultat.KANSKJE, operator = Resultat::eller, y = Resultat.JA, expected = Resultat.JA),
        Case(x = Resultat.KANSKJE, operator = Resultat::eller, y = Resultat.NEI, expected = Resultat.KANSKJE),
        Case(x = Resultat.KANSKJE, operator = Resultat::eller, y = Resultat.KANSKJE, expected = Resultat.KANSKJE),
    )

    @Test
    fun ikke() {
        Resultat.JA.ikke() shouldBe Resultat.NEI
        Resultat.NEI.ikke() shouldBe Resultat.JA
        Resultat.KANSKJE.ikke() shouldBe Resultat.KANSKJE
    }

    private fun test(vararg cases: Case): Stream<DynamicTest> = DynamicTest.stream(cases.iterator(), Case::toString) {
        it.operator(it.x, it.y) shouldBe it.expected
    }

    class Case(
        val x: Resultat,
        val operator: KFunction2<Resultat, Resultat, Resultat>,
        val y: Resultat,
        val expected: Resultat,
    ) {
        override fun toString(): String = "$x ${operator.name} $y = $expected"
    }
}
