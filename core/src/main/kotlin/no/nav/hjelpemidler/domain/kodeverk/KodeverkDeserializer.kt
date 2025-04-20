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
    private val wrapped: JsonDeserializer<*>? = null,
) : StdScalarDeserializer<Kodeverk<*>>(Kodeverk::class.java), ContextualDeserializer {
    override fun logicalType(): LogicalType = LogicalType.Enum

    override fun deserialize(parser: JsonParser, context: DeserializationContext): Kodeverk<*> {
        if (wrapped == null) {
            throw JsonMappingException(parser, "KodeverkDeserializer.wrapped ikke satt")
        }
        return try {
            wrapped.deserialize(parser, context) as Kodeverk<*>
        } catch (e: InvalidFormatException) {
            UkjentKode(e.value.toString())
        }
    }

    override fun createContextual(context: DeserializationContext, property: BeanProperty?): JsonDeserializer<*> {
        if (property == null) return this
        val valueType = property.type.containedType(0)
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
