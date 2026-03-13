package no.nav.hjelpemidler.serialization.jackson.core

import com.fasterxml.jackson.annotation.JsonFormat
import no.nav.hjelpemidler.domain.Maybe
import no.nav.hjelpemidler.domain.kodeverk.Kodeverk
import tools.jackson.databind.BeanDescription
import tools.jackson.databind.DeserializationConfig
import tools.jackson.databind.JavaType
import tools.jackson.databind.MapperFeature
import tools.jackson.databind.SerializationConfig
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.deser.Deserializers
import tools.jackson.databind.jsontype.TypeDeserializer
import tools.jackson.databind.jsontype.TypeSerializer
import tools.jackson.databind.module.SimpleModule
import tools.jackson.databind.ser.Serializers
import tools.jackson.databind.type.ReferenceType

internal class CoreModule : SimpleModule() {
    override fun setupModule(context: SetupContext) {
        super.setupModule(context)
        with(context) {
            addSerializers(CoreSerializers)
            addDeserializers(CoreDeserializers)
            addTypeModifier(MaybeTypeModifier)
        }
    }
}

internal object CoreSerializers : Serializers.Base() {
    override fun findReferenceSerializer(
        config: SerializationConfig,
        type: ReferenceType,
        beanDescriptionRef: BeanDescription.Supplier?,
        formatOverrides: JsonFormat.Value?,
        valueTypeSerializer: TypeSerializer?,
        valueSerializer: ValueSerializer<Any?>?,
    ): ValueSerializer<*>? {
        if (Maybe::class.java.isAssignableFrom(type.rawClass)) {
            val staticTyping = valueTypeSerializer == null && config.isEnabled(MapperFeature.USE_STATIC_TYPING)
            return MaybeSerializer(type, staticTyping, valueTypeSerializer, valueSerializer)
        }
        return null
    }
}

internal object CoreDeserializers : Deserializers.Base() {
    override fun findBeanDeserializer(
        type: JavaType,
        config: DeserializationConfig?,
        beanDescriptionRef: BeanDescription.Supplier?,
    ): ValueDeserializer<*>? {
        if (Kodeverk::class.java.isAssignableFrom(type.rawClass)) {
            return KodeverkDeserializer()
        }
        return null
    }

    override fun findReferenceDeserializer(
        type: ReferenceType,
        config: DeserializationConfig,
        beanDescriptionRef: BeanDescription.Supplier?,
        valueTypeDeserializer: TypeDeserializer?,
        valueDeserializer: ValueDeserializer<*>?,
    ): ValueDeserializer<*>? {
        if (Maybe::class.java.isAssignableFrom(type.rawClass)) {
            return MaybeDeserializer(type, null, valueTypeDeserializer, valueDeserializer)
        }
        return null
    }

    override fun hasDeserializerFor(
        config: DeserializationConfig,
        valueType: Class<*>,
    ): Boolean = when (valueType) {
        Kodeverk::class.java,
        Maybe::class.java,
            -> true

        else -> false
    }
}
