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
    private val enumType: JavaType?
    private val enumDeserializer: ValueDeserializer<*>?

    constructor() : super(Kodeverk::class.java) {
        this.enumType = null
        this.enumDeserializer = null
    }

    constructor(
        source: KodeverkDeserializer,
        enumType: JavaType,
        enumDeserializer: ValueDeserializer<*>,
    ) : super(source) {
        this.enumType = enumType
        this.enumDeserializer = enumDeserializer
    }

    override fun createContextual(context: DeserializationContext, property: BeanProperty?): ValueDeserializer<*> {
        val parentType = property?.type ?: context.contextualType
        val enumType = parentType.containedTypeOrUnknown(0)
        val enumDeserializer = context.findContextualValueDeserializer(enumType, property)
        if (enumType == this.enumType && enumDeserializer == this.enumDeserializer) return this
        return KodeverkDeserializer(this, enumType, enumDeserializer)
    }

    override fun deserialize(parser: JsonParser, context: DeserializationContext): Kodeverk<*> {
        checkNotNull(enumDeserializer)
        return try {
            enumDeserializer.deserialize(parser, context) as Kodeverk<*>
        } catch (e: InvalidFormatException) {
            UkjentKode(e.value.toString())
        }
    }

    override fun logicalType(): LogicalType = LogicalType.Enum

    override fun isCachable(): Boolean = true
}
