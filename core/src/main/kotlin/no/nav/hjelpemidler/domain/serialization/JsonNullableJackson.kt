package no.nav.hjelpemidler.domain.serialization

import no.nav.hjelpemidler.domain.id.Id
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
import java.math.BigDecimal
import java.math.BigInteger

internal class JsonNullableSerializer : StdSerializer<JsonNullable<*>> {
    private val childType: JavaType?
    private val childSerializer: ValueSerializer<Any?>?

    constructor() : super(JsonNullable::class.java) {
        this.childType = null
        this.childSerializer = null
    }

    constructor(
        source: JsonNullableSerializer,
        childType: JavaType,
        childSerializer: ValueSerializer<Any?>,
    ) : super(source) {
        this.childType = childType
        this.childSerializer = childSerializer
    }

    override fun createContextual(context: SerializationContext, property: BeanProperty?): ValueSerializer<*> {
        val parentType = property?.type ?: return this
        val childType = parentType.containedTypeOrUnknown(0)
        val childSerializer = context.findValueSerializer(childType)
        if (childType == this.childType && childSerializer == this.childSerializer) return this
        return JsonNullableSerializer(this, childType, childSerializer)
    }

    override fun serialize(value: JsonNullable<Any?>, generator: JsonGenerator, context: SerializationContext) {
        if (value !is JsonNullable.Present || value.value == null) {
            generator.writeNull()
        } else if (childSerializer == null) {
            when (val v = value.value) {
                is Boolean -> generator.writeBoolean(v)

                is String -> generator.writeString(v)

                is Int -> generator.writeNumber(v)
                is Long -> generator.writeNumber(v)
                is Float -> generator.writeNumber(v)
                is Double -> generator.writeNumber(v)
                is BigInteger -> generator.writeNumber(v)
                is BigDecimal -> generator.writeNumber(v)

                is Id<*> -> generator.writeString(v.toString())

                else -> generator.writePOJO(v)
            }
        } else {
            childSerializer.serialize(value.value, generator, context)
        }
    }

    override fun isEmpty(context: SerializationContext, value: JsonNullable<*>?): Boolean =
        value == null || value is JsonNullable.Undefined
}

internal class JsonNullableDeserializer : StdDeserializer<JsonNullable<*>> {
    private val childType: JavaType?
    private val childDeserializer: ValueDeserializer<*>?

    constructor() : super(JsonNullable::class.java) {
        this.childType = null
        this.childDeserializer = null
    }

    constructor(
        source: JsonNullableDeserializer,
        childType: JavaType,
        childDeserializer: ValueDeserializer<*>,
    ) : super(source) {
        this.childType = childType
        this.childDeserializer = childDeserializer
    }

    override fun createContextual(context: DeserializationContext, property: BeanProperty?): ValueDeserializer<*> {
        val parentType = property?.type ?: context.contextualType
        val childType = parentType.containedTypeOrUnknown(0)
        val childDeserializer = context.findContextualValueDeserializer(childType, property)
        if (childType == this.childType && childDeserializer == this.childDeserializer) return this
        return JsonNullableDeserializer(this, childType, childDeserializer)
    }

    override fun deserialize(parser: JsonParser, context: DeserializationContext): JsonNullable<*> {
        checkNotNull(childDeserializer)
        val value = childDeserializer.deserialize(parser, context)
        return JsonNullable.Present(value)
    }

    override fun isCachable(): Boolean = true

    override fun getNullValue(context: DeserializationContext): Any = JsonNullable.Null

    override fun getAbsentValue(context: DeserializationContext): Any = JsonNullable.Undefined
}
