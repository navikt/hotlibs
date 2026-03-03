package no.nav.hjelpemidler.domain.kodeverk

import tools.jackson.core.JsonParser
import tools.jackson.databind.BeanProperty
import tools.jackson.databind.DatabindException
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.deser.std.StdScalarDeserializer
import tools.jackson.databind.exc.InvalidFormatException
import tools.jackson.databind.type.LogicalType

internal class KodeverkDeserializer(
    private val enumDeserializer: ValueDeserializer<*>? = null,
) : StdScalarDeserializer<Kodeverk<*>>(Kodeverk::class.java) {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): Kodeverk<*> {
        if (enumDeserializer == null) {
            throw DatabindException.from(parser, "enumDeserializer var null")
        }
        return try {
            enumDeserializer.deserialize(parser, context) as Kodeverk<*>
        } catch (e: InvalidFormatException) {
            UkjentKode(e.value.toString())
        }
    }

    override fun createContextual(context: DeserializationContext, property: BeanProperty?): ValueDeserializer<*> {
        TODO() // fixme
    }

    override fun logicalType(): LogicalType = LogicalType.Enum

    override fun isCachable(): Boolean = true
}
