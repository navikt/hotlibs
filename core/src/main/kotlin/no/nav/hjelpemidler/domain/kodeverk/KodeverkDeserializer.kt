package no.nav.hjelpemidler.domain.kodeverk

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer
import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.type.LogicalType

class KodeverkDeserializer(
    private val enumDeserializer: JsonDeserializer<*>? = null,
) : StdScalarDeserializer<Kodeverk<*>>(Kodeverk::class.java), ContextualDeserializer {
    override fun logicalType(): LogicalType = LogicalType.Enum

    override fun deserialize(parser: JsonParser, context: DeserializationContext): Kodeverk<*> {
        if (enumDeserializer == null) {
            throw JsonMappingException.from(parser, "enumDeserializer var null")
        }
        return try {
            enumDeserializer.deserialize(parser, context) as Kodeverk<*>
        } catch (e: InvalidFormatException) {
            UkjentKode(e.value.toString())
        }
    }

    override fun createContextual(context: DeserializationContext, property: BeanProperty?): JsonDeserializer<*> {
        if (property == null) return this
        val propertyType = property.type
        val valueType = propertyType.containedType(0)
            ?: throw JsonMappingException.from(context, "valueType var null, propertyType: $propertyType")
        return KodeverkDeserializer(
            context.factory.createEnumDeserializer(
                context,
                valueType,
                context.config.introspect(valueType),
            ),
        )
    }

    override fun isCachable(): Boolean = true
}
