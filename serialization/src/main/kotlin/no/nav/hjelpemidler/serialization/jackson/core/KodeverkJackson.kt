package no.nav.hjelpemidler.serialization.jackson.core

import no.nav.hjelpemidler.domain.kodeverk.Kodeverk
import no.nav.hjelpemidler.domain.kodeverk.Tekstkode
import no.nav.hjelpemidler.serialization.jackson.error
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

    private constructor(
        source: KodeverkDeserializer,
        enumType: JavaType,
        enumDeserializer: ValueDeserializer<*>,
    ) : super(source) {
        this.enumType = enumType
        this.enumDeserializer = enumDeserializer
    }

    override fun createContextual(context: DeserializationContext, property: BeanProperty?): ValueDeserializer<*> {
        val kodeverkType = context.contextualType
            ?: property?.type
            ?: context.error("Could not determine Kodeverk type")
        val enumType = kodeverkType.findTypeParameters(Kodeverk::class.java).firstOrNull()
            ?: context.error("Could not determine enum type for $kodeverkType")
        val enumDeserializer = context.findContextualValueDeserializer(enumType, property)
        if (enumType == this.enumType && enumDeserializer == this.enumDeserializer) return this
        return KodeverkDeserializer(this, enumType, enumDeserializer)
    }

    override fun deserialize(parser: JsonParser, context: DeserializationContext): Kodeverk<*>? {
        val deserializer = enumDeserializer
            ?: context.error("KodeverkDeserializer used without contextualization (enumDeserializer is null)")
        return try {
            when (val value = deserializer.deserialize(parser, context)) {
                null -> null
                is Kodeverk<*> -> value
                else -> context.error("${value.javaClass.name} does not implement Kodeverk")
            }
        } catch (e: InvalidFormatException) {
            val value = e.value ?: context.error("Could not determine unknown enum name, exception value was null", e)
            val name = value.toString()
            if (name.isBlank()) throw e
            Tekstkode<Nothing>(name)
        }
    }

    override fun logicalType(): LogicalType = LogicalType.Enum

    override fun isCachable(): Boolean = true
}
