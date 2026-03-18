package no.nav.hjelpemidler.serialization.jackson

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
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.deser.std.FromStringDeserializer
import tools.jackson.databind.module.SimpleModule
import tools.jackson.databind.ser.std.ToStringSerializer
import tools.jackson.module.kotlin.addDeserializer
import kotlin.reflect.KClass

internal class ThreeTenExtraModule : SimpleModule() {
    init {
        setOf(
            // Temporal
            OffsetDate::class,
            HourMinute::class,
            YearWeek::class,
            YearQuarter::class,
            YearHalf::class,

            // TemporalAmount
            Seconds::class,
            Minutes::class,
            Hours::class,
            Days::class,
            Weeks::class,
            Months::class,
            Years::class,
            PeriodDuration::class,

            // Range
            LocalDateRange::class,

            // Interval
            Interval::class,
        ).forEach { type -> addSerializer(type.java, ToStringSerializer.instance) }

        // Temporal
        addDeserializer(OffsetDate::class, OffsetDate::parse)
        addDeserializer(HourMinute::class, HourMinute::parse)
        addDeserializer(YearWeek::class, YearWeek::parse)
        addDeserializer(YearQuarter::class, YearQuarter::parse)
        addDeserializer(YearHalf::class, YearHalf::parse)

        // TemporalAmount
        addDeserializer(Seconds::class, Seconds::parse)
        addDeserializer(Minutes::class, Minutes::parse)
        addDeserializer(Hours::class, Hours::parse)
        addDeserializer(Days::class, Days::parse)
        addDeserializer(Weeks::class, Weeks::parse)
        addDeserializer(Months::class, Months::parse)
        addDeserializer(Years::class, Years::parse)
        addDeserializer(PeriodDuration::class, PeriodDuration::parse)

        // Range
        addDeserializer(LocalDateRange::class, LocalDateRange::parse)

        // Interval
        addDeserializer(Interval::class, Interval::parse)
    }

    private inline fun <reified T : Any> addDeserializer(type: KClass<T>, noinline creator: (rawValue: String) -> T) =
        addDeserializer<T>(type, Deserializer(type, creator)) // fixme -> se på denne

    class Deserializer<T : Any>(type: KClass<T>, private val creator: (rawValue: String) -> T) :
        FromStringDeserializer<T>(type.java) {
        override fun _deserialize(value: String, context: DeserializationContext): T = creator(value)
    }
}
