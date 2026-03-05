package no.nav.hjelpemidler.domain.serialization

import tools.jackson.core.JsonGenerator
import tools.jackson.core.JsonParser
import tools.jackson.databind.BeanProperty
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JavaType
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.deser.std.StdDeserializer
import tools.jackson.databind.ser.std.StdSerializer

internal class JsonNullableSerializer : StdSerializer<JsonNullable<*>> {
    private val valueType: JavaType?
    private val valueSerializer: ValueSerializer<Any?>?

    constructor() : super(JsonNullable::class.java) {
        this.valueType = null
        this.valueSerializer = null
    }

    constructor(
        source: JsonNullableSerializer,
        valueType: JavaType,
        valueSerializer: ValueSerializer<Any?>,
    ) : super(source) {
        this.valueType = valueType
        this.valueSerializer = valueSerializer
    }

    override fun createContextual(context: SerializationContext, property: BeanProperty?): ValueSerializer<*> {
        val type = property?.type ?: TODO()
        val valueType = type.containedTypeOrUnknown(0)
        val valueSerializer = context.findValueSerializer(valueType)
        if (valueType == this.valueType && valueSerializer == this.valueSerializer) return this
        return JsonNullableSerializer(this, valueType, valueSerializer)
    }

    override fun serialize(value: JsonNullable<Any?>, generator: JsonGenerator, context: SerializationContext) {
        if (value !is JsonNullable.Present || value.value == null) {
            generator.writeNull()
        } else {
            checkNotNull(valueSerializer)
            valueSerializer.serialize(value.value, generator, context)
        }
    }

    override fun isEmpty(context: SerializationContext, value: JsonNullable<*>?): Boolean =
        value == null || value is JsonNullable.Undefined
}

internal class JsonNullableDeserializer : StdDeserializer<JsonNullable<*>> {
    private val valueType: JavaType?
    private val valueDeserializer: ValueDeserializer<*>?

    constructor() : super(JsonNullable::class.java) {
        this.valueType = null
        this.valueDeserializer = null
    }

    constructor(
        source: JsonNullableDeserializer,
        valueType: JavaType,
        valueDeserializer: ValueDeserializer<*>,
    ) : super(source) {
        this.valueType = valueType
        this.valueDeserializer = valueDeserializer
    }

    override fun createContextual(context: DeserializationContext, property: BeanProperty?): ValueDeserializer<*> {
        val type = property?.type ?: context.contextualType
        val valueType = type.containedTypeOrUnknown(0)
        val valueDeserializer = context.findContextualValueDeserializer(valueType, null)
        if (valueType == this.valueType && valueDeserializer == this.valueDeserializer) return this
        return JsonNullableDeserializer(this, valueType, valueDeserializer)
    }

    override fun deserialize(parser: JsonParser, context: DeserializationContext): JsonNullable<*> {
        checkNotNull(valueDeserializer)
        val value = valueDeserializer.deserialize(parser, context)
        return JsonNullable.Present(value)
    }

    override fun isCachable(): Boolean = true

    override fun getNullValue(context: DeserializationContext): Any = JsonNullable.Null

    override fun getAbsentValue(context: DeserializationContext): Any = JsonNullable.Undefined
}
