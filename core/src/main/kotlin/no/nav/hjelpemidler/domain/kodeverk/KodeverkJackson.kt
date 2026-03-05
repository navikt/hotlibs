package no.nav.hjelpemidler.domain.kodeverk

import tools.jackson.core.JsonParser
import tools.jackson.databind.BeanProperty
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JavaType
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.deser.std.StdScalarDeserializer
import tools.jackson.databind.exc.InvalidFormatException
import tools.jackson.databind.type.LogicalType

internal class KodeverkDeserializer : StdScalarDeserializer<Kodeverk<*>> {
    private val valueType: JavaType?
    private val valueDeserializer: ValueDeserializer<*>?

    constructor() : super(Kodeverk::class.java) {
        this.valueType = null
        this.valueDeserializer = null
    }

    constructor(
        source: KodeverkDeserializer,
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
        return KodeverkDeserializer(this, valueType, valueDeserializer)
    }

    override fun deserialize(parser: JsonParser, context: DeserializationContext): Kodeverk<*> {
        checkNotNull(valueDeserializer)
        return try {
            valueDeserializer.deserialize(parser, context) as Kodeverk<*>
        } catch (e: InvalidFormatException) {
            UkjentKode(e.value.toString())
        }
    }

    override fun logicalType(): LogicalType = LogicalType.Enum

    override fun isCachable(): Boolean = true
}
