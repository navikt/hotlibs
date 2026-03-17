package no.nav.hjelpemidler.serialization.jackson.core

import no.nav.hjelpemidler.core.Maybe
import no.nav.hjelpemidler.core.Maybe.Absent
import tools.jackson.databind.BeanProperty
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JavaType
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.deser.ValueInstantiator
import tools.jackson.databind.deser.std.ReferenceTypeDeserializer
import tools.jackson.databind.jsontype.TypeDeserializer
import tools.jackson.databind.jsontype.TypeSerializer
import tools.jackson.databind.ser.std.ReferenceTypeSerializer
import tools.jackson.databind.type.ReferenceType
import tools.jackson.databind.type.TypeBindings
import tools.jackson.databind.type.TypeFactory
import tools.jackson.databind.type.TypeModifier
import tools.jackson.databind.util.NameTransformer
import java.lang.reflect.Type

internal class MaybeSerializer : ReferenceTypeSerializer<Maybe<*>> {
    constructor(
        fullType: ReferenceType,
        staticTyping: Boolean,
        valueTypeSerializer: TypeSerializer?,
        valueSerializer: ValueSerializer<Any?>?,
    ) : super(fullType, staticTyping, valueTypeSerializer, valueSerializer)

    constructor(
        base: MaybeSerializer,
        property: BeanProperty?,
        valueTypeSerializer: TypeSerializer?,
        valueSerializer: ValueSerializer<*>?,
        unwrapper: NameTransformer?,
        suppressableValue: Any?,
        suppressNulls: Boolean,
    ) : super(base, property, valueTypeSerializer, valueSerializer, unwrapper, suppressableValue, suppressNulls)

    override fun withResolved(
        property: BeanProperty?,
        valueTypeSerializer: TypeSerializer?,
        valueSerializer: ValueSerializer<*>?,
        unwrapper: NameTransformer?,
    ): ReferenceTypeSerializer<Maybe<*>> = MaybeSerializer(
        this,
        property,
        valueTypeSerializer,
        valueSerializer,
        unwrapper,
        _suppressableValue,
        _suppressNulls,
    )

    override fun withContentInclusion(
        suppressableValue: Any?,
        suppressNulls: Boolean,
    ): ReferenceTypeSerializer<Maybe<*>> = MaybeSerializer(
        this,
        _property,
        _valueTypeSerializer,
        _valueSerializer,
        _unwrapper,
        suppressableValue,
        suppressNulls,
    )

    override fun _isValuePresent(value: Maybe<*>): Boolean = value.isPresent()

    override fun _getReferenced(value: Maybe<*>): Any? = value.getOrNull()

    override fun _getReferencedIfPresent(value: Maybe<*>): Any? = value.getOrNull()
}

internal class MaybeDeserializer : ReferenceTypeDeserializer<Maybe<*>> {
    constructor(
        fullType: JavaType,
        valueInstantiator: ValueInstantiator?,
        valueTypeDeserializer: TypeDeserializer?,
        valueDeserializer: ValueDeserializer<*>?,
    ) : super(fullType, valueInstantiator, valueTypeDeserializer, valueDeserializer)

    override fun withResolved(
        valueTypeDeserializer: TypeDeserializer?,
        valueDeserializer: ValueDeserializer<*>?,
    ): ReferenceTypeDeserializer<Maybe<*>> =
        MaybeDeserializer(_fullType, _valueInstantiator, valueTypeDeserializer, valueDeserializer)

    override fun getNullValue(context: DeserializationContext): Maybe<*> =
        Maybe(_valueDeserializer.getNullValue(context))

    override fun getEmptyValue(context: DeserializationContext): Any = Absent

    override fun getAbsentValue(context: DeserializationContext): Any = Absent

    override fun referenceValue(contents: Any?): Maybe<*> = Maybe(contents)

    override fun updateReference(reference: Maybe<*>, contents: Any?): Maybe<*> = Maybe(contents)

    override fun getReferenced(reference: Maybe<*>): Any? = reference.getOrNull()
}

internal object MaybeTypeModifier : TypeModifier() {
    override fun modifyType(type: JavaType, jdkType: Type, context: TypeBindings, typeFactory: TypeFactory): JavaType {
        if (type.isReferenceType || type.isContainerType || type.rawClass != Maybe::class.java) {
            return type
        }
        return ReferenceType.upgradeFrom(type, type.containedTypeOrUnknown(0))
    }
}
