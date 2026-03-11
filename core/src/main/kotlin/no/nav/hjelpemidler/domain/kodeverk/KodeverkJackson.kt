package no.nav.hjelpemidler.domain.kodeverk

import no.nav.hjelpemidler.domain.serialization.error
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
        val parentType = property?.type ?: context.contextualType ?: context.error("Could not determine parent type")
        val enumType = parentType.containedType(0) ?: context.error("Could not determine enum type")
        val enumDeserializer = context.findContextualValueDeserializer(enumType, property)
        if (enumType == this.enumType && enumDeserializer == this.enumDeserializer) return this
        return KodeverkDeserializer(this, enumType, enumDeserializer)
    }

    override fun deserialize(parser: JsonParser, context: DeserializationContext): Kodeverk<*> {
        val deserializer = enumDeserializer
            ?: context.error("KodeverkDeserializer used without contextualization (enumDeserializer is null)")
        return try {
            deserializer.deserialize(parser, context) as Kodeverk<*>
        } catch (e: InvalidFormatException) {
            UkjentKode(e.value.toString())
        }
    }

    override fun logicalType(): LogicalType = LogicalType.Enum

    override fun isCachable(): Boolean = true
}
