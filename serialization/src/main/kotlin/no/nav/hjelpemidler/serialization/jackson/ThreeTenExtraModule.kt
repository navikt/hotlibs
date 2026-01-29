package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.threeten.extra.Interval

internal class IntervalDeserializer : FromStringDeserializer<Interval>(Interval::class.java) {
    override fun _deserialize(value: String, context: DeserializationContext): Interval = Interval.parse(value)
}

internal class ThreeTenExtraModule : SimpleModule() {
    init {
        addSerializer(Interval::class.java, ToStringSerializer())
        addDeserializer(Interval::class.java, IntervalDeserializer())
    }
}
