package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.fasterxml.jackson.module.kotlin.addDeserializer
import org.threeten.extra.Days
import org.threeten.extra.HourMinute
import org.threeten.extra.Hours
import org.threeten.extra.Interval
import org.threeten.extra.LocalDateRange
import org.threeten.extra.Minutes
import org.threeten.extra.Months
import org.threeten.extra.OffsetDate
import org.threeten.extra.PeriodDuration
import org.threeten.extra.Seconds
import org.threeten.extra.Weeks
import org.threeten.extra.YearHalf
import org.threeten.extra.YearQuarter
import org.threeten.extra.YearWeek
import org.threeten.extra.Years
import kotlin.reflect.KClass

internal class ThreeTenExtraModule : SimpleModule() {
    init {
        setOf(
            Days::class,
            HourMinute::class,
            Hours::class,
            Interval::class,
            LocalDateRange::class,
            Minutes::class,
            Months::class,
            OffsetDate::class,
            PeriodDuration::class,
            Seconds::class,
            Weeks::class,
            YearHalf::class,
            YearQuarter::class,
            YearWeek::class,
            Years::class,
        ).forEach { type -> addSerializer(type.java, ToStringSerializer()) }

        addDeserializer(Days::class, DaysDeserializer())
        addDeserializer(HourMinute::class, HourMinuteDeserializer())
        addDeserializer(Hours::class, HoursDeserializer())
        addDeserializer(Interval::class, IntervalDeserializer())
        addDeserializer(LocalDateRange::class, LocalDateRangeDeserializer())
        addDeserializer(Minutes::class, MinutesDeserializer())
        addDeserializer(Months::class, MonthsDeserializer())
        addDeserializer(OffsetDate::class, OffsetDateDeserializer())
        addDeserializer(PeriodDuration::class, PeriodDurationDeserializer())
        addDeserializer(Seconds::class, SecondsDeserializer())
        addDeserializer(Weeks::class, WeeksDeserializer())
        addDeserializer(YearHalf::class, YearHalfDeserializer())
        addDeserializer(YearQuarter::class, YearQuarterDeserializer())
        addDeserializer(YearWeek::class, YearWeekDeserializer())
        addDeserializer(Years::class, YearsDeserializer())
    }

    class DaysDeserializer : Deserializer<Days>(Days::class, Days::parse)
    class HourMinuteDeserializer : Deserializer<HourMinute>(HourMinute::class, HourMinute::parse)
    class HoursDeserializer : Deserializer<Hours>(Hours::class, Hours::parse)
    class IntervalDeserializer : Deserializer<Interval>(Interval::class, Interval::parse)
    class LocalDateRangeDeserializer : Deserializer<LocalDateRange>(LocalDateRange::class, LocalDateRange::parse)
    class MinutesDeserializer : Deserializer<Minutes>(Minutes::class, Minutes::parse)
    class MonthsDeserializer : Deserializer<Months>(Months::class, Months::parse)
    class OffsetDateDeserializer : Deserializer<OffsetDate>(OffsetDate::class, OffsetDate::parse)
    class PeriodDurationDeserializer : Deserializer<PeriodDuration>(PeriodDuration::class, PeriodDuration::parse)
    class SecondsDeserializer : Deserializer<Seconds>(Seconds::class, Seconds::parse)
    class WeeksDeserializer : Deserializer<Weeks>(Weeks::class, Weeks::parse)
    class YearHalfDeserializer : Deserializer<YearHalf>(YearHalf::class, YearHalf::parse)
    class YearQuarterDeserializer : Deserializer<YearQuarter>(YearQuarter::class, YearQuarter::parse)
    class YearWeekDeserializer : Deserializer<YearWeek>(YearWeek::class, YearWeek::parse)
    class YearsDeserializer : Deserializer<Years>(Years::class, Years::parse)

    abstract class Deserializer<T : Any>(
        type: KClass<T>,
        private val creator: (rawValue: String) -> T,
    ) : FromStringDeserializer<T>(type.java) {
        override fun _deserialize(value: String, context: DeserializationContext): T = creator(value)
    }
}
