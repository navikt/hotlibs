package no.nav.hjelpemidler.domain.kodeverk

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.ContextualDeserializer
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer
import com.fasterxml.jackson.databind.type.LogicalType
import com.fasterxml.jackson.databind.util.ClassUtil

class KodeverkDeserializer(type: JavaType? = null) : StdScalarDeserializer<Kodeverk<*>>(type), ContextualDeserializer {
    override fun logicalType(): LogicalType = LogicalType.Enum

    override fun deserialize(parser: JsonParser, context: DeserializationContext): Kodeverk<*> {
        val text = parser.text
        if (valueType == null) {
            return UkjentKode(text)
        }
        return try {
            val enumType = ClassUtil.findEnumType(valueType.rawClass)
            java.lang.Enum.valueOf(enumType, text) as Kodeverk<*>
        } catch (e: IllegalArgumentException) {
            UkjentKode(text)
        }
    }

    override fun createContextual(context: DeserializationContext, property: BeanProperty?): JsonDeserializer<*> {
        if (property == null) return this
        val wrapperType = property.type
        val valueType = wrapperType.containedType(0)
        return KodeverkDeserializer(valueType)
    }
}
