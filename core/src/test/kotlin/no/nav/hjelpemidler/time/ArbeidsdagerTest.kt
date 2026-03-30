package no.nav.hjelpemidler.time

import io.kotest.matchers.shouldBe
import no.bekk.bekkopen.date.NorwegianDateUtil
import no.nav.hjelpemidler.test.testFactory
import org.junit.jupiter.api.TestFactory
import java.time.LocalDate
import java.time.Month
import java.util.Date

class ArbeidsdagerTest {
    private val start = LocalDate
        .of(2026, Month.JANUARY, 1)
        .atStartOfDay(ZONE_ID_EUROPE_OSLO)

    @TestFactory
    fun test() = testFactory(
        (0..100).asSequence(),
        { "$it arbeidsdager" },
    ) {
        val expected = NorwegianDateUtil
            .addWorkingDaysToDate(Date.from(start.toInstant()), it)
            .toInstant()
            .atZone(ZONE_ID_EUROPE_OSLO)

        (start + it.arbeidsdager) shouldBe expected
    }
}
