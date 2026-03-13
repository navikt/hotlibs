package no.nav.hjelpemidler.serialization.jackson.core

import no.nav.hjelpemidler.domain.kodeverk.Kodeverk
import no.nav.hjelpemidler.domain.kodeverk.UkjentKode
import no.nav.hjelpemidler.serialization.jackson.error
import tools.jackson.core.JsonParser
import tools.jackson.core.JsonToken
import tools.jackson.databind.BeanProperty
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JavaType
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.deser.std.StdScalarDeserializer
import tools.jackson.databind.introspect.AnnotatedClassResolver
import tools.jackson.databind.type.LogicalType
import tools.jackson.databind.util.EnumResolver

internal class KodeverkDeserializer : StdScalarDeserializer<Kodeverk<*>> {
    private val enumType: JavaType?
    private val enumResolver: EnumResolver?

    constructor() : super(Kodeverk::class.java) {
        this.enumType = null
        this.enumResolver = null
    }

    constructor(source: KodeverkDeserializer, enumType: JavaType, enumResolver: EnumResolver) : super(source) {
        this.enumType = enumType
        this.enumResolver = enumResolver
    }

    override fun createContextual(context: DeserializationContext, property: BeanProperty?): ValueDeserializer<*> {
        val parentType = property?.type ?: context.contextualType ?: context.error("Could not determine parent type")
        val enumType = parentType.containedType(0) ?: context.error("Could not determine enum type")
        val annotatedClass = AnnotatedClassResolver.resolve(context.config, enumType, context.config)
        val enumResolver = EnumResolver.constructFor(context.config, annotatedClass)
        return KodeverkDeserializer(this, enumType, enumResolver)
    }

    override fun deserialize(parser: JsonParser, context: DeserializationContext): Kodeverk<*> {
        val currentToken = parser.currentToken()
        if (currentToken != JsonToken.VALUE_STRING) {
            context.reportWrongTokenException(enumType, JsonToken.VALUE_STRING, null)
        }
        val resolver =
            enumResolver ?: context.error("KodeverkDeserializer used without contextualization (enumResolver is null)")
        val stringValue = parser.string
        val enumValue = resolver.findEnum(stringValue) as Kodeverk<*>?
        return enumValue ?: UkjentKode(stringValue)
    }

    override fun logicalType(): LogicalType = LogicalType.Enum

    override fun isCachable(): Boolean = true
}
