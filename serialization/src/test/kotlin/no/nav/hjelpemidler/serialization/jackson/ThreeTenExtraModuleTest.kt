package no.nav.hjelpemidler.serialization.jackson

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.text.doubleQuoted
import org.threeten.extra.Interval
import org.threeten.extra.LocalDateRange
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeParseException
import kotlin.test.Test

class ThreeTenExtraModuleTest {
    private val now = Instant.now()
    private val today = LocalDate.now()
    private val interval = Interval.of(now.minusSeconds(3_600), now)
    private val intervalJson = "${interval.start}/${interval.end}".doubleQuoted()

    @Test
    fun `Deserialiser unbounded LocalDateRange til Kotlin med Jackson`() {
        jsonToValue<LocalDateRange>("/".doubleQuoted()) shouldBe LocalDateRange.ALL
        jsonToValue<LocalDateRange>("/$today".doubleQuoted()) shouldBe LocalDateRange.ofUnboundedStart(today)
        jsonToValue<LocalDateRange>("$today/".doubleQuoted()) shouldBe LocalDateRange.ofUnboundedEnd(today)
    }

    @Test
    fun `Serialiser Interval til JSON med Jackson`() {
        valueToJson(interval) shouldBe intervalJson
    }

    @Test
    fun `Deserialiser Interval til Kotlin med Jackson`() {
        jsonToValue<Interval?>("""null""") shouldBe null
        jsonToValue<Interval>(intervalJson) shouldBe interval
        jsonToValue<Interval>("PT3600S/$now".doubleQuoted()) shouldBe interval
        jsonToValue<Interval>("$now/PT3600S".doubleQuoted()) shouldBe interval
            .withStart(now)
            .withEnd(now.plusSeconds(3_600))
        shouldThrow<DateTimeParseException> { jsonToValue<Interval>(""""foobar"""") }
    }

    @Test
    fun `Deserialiser unbounded Interval til Kotlin med Jackson`() {
        jsonToValue<Interval>("/".doubleQuoted()) shouldBe Interval.ALL
        jsonToValue<Interval>("/$now".doubleQuoted()) shouldBe Interval.endingAt(now)
        jsonToValue<Interval>("$now/".doubleQuoted()) shouldBe Interval.startingAt(now)
    }
}
