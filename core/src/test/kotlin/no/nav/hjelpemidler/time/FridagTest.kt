package no.nav.hjelpemidler.time

import io.kotest.matchers.shouldBe
import no.bekk.bekkopen.date.NorwegianDateUtil
import no.nav.hjelpemidler.test.testFactory
import org.junit.jupiter.api.TestFactory

class FridagTest {
    @TestFactory
    fun test() = testFactory(
        (2000..2200).asSequence(),
        { år -> "$år" },
    ) { år ->
        val palmesøndag = utledFørstePåskedagI(år) - 7.dager
        val expected = NorwegianDateUtil
            .getHolidays(år)
            .filterNot { it.toInstant().asLocalDate() == palmesøndag } // palmesøndag er en vanlig søndag
            .mapTo(sortedSetOf()) { it.toInstant().asLocalDate() }

        val actual = fridagerI(år)

        actual shouldBe expected
    }
}
