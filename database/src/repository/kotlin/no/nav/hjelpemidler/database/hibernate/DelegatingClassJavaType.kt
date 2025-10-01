package no.nav.hjelpemidler.database.hibernate

import jakarta.persistence.AttributeConverter
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.type.descriptor.java.AbstractClassJavaType
import org.hibernate.type.descriptor.java.JavaType
import kotlin.reflect.KClass

/**
 * Bruk [AttributeConverter] som [JavaType].
 */
abstract class DelegatingClassJavaType<X : Any, Y : Any>(
    type: KClass<X>,
    private val converter: AttributeConverter<X, Y>,
    private val delegate: JavaType<Y>,
) : AbstractClassJavaType<X>(type.java) {
    @Suppress("UNCHECKED_CAST")
    override fun <Z : Any> unwrap(value: X?, type: Class<Z>, options: WrapperOptions): Z? = when {
        value == null -> null
        type.isInstance(value) -> value as Z
        else -> delegate.unwrap(converter.convertToDatabaseColumn(value), type, options)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <Z : Any> wrap(value: Z?, options: WrapperOptions): X? = when {
        value == null -> null
        javaType.isInstance(value) -> value as X
        else -> converter.convertToEntityAttribute(delegate.wrap(value, options))
    }
}

fun <X : Any, Z : Any> JavaType<X>.unwrap(value: X?, type: KClass<Z>, options: WrapperOptions): Z? =
    unwrap(value, type.java, options)
