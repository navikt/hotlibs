package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.domain.ValueType
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.type.descriptor.java.AbstractClassJavaType
import org.hibernate.type.descriptor.java.JavaType
import kotlin.reflect.KClass

abstract class ValueJavaType<T : ValueType<R>, R : Comparable<R>>(
    type: KClass<T>,
    private val delegate: JavaType<R>,
    private val factory: (R) -> T,
) : AbstractClassJavaType<T>(type.java) {
    @Suppress("UNCHECKED_CAST")
    override fun <X : Any> unwrap(value: T?, type: Class<X>, options: WrapperOptions): X? = when {
        value == null -> null
        type.isInstance(value) -> value as X
        else -> delegate.unwrap(value.value, type, options)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <X : Any> wrap(value: X?, options: WrapperOptions): T? = when {
        value == null -> null
        javaType.isInstance(value) -> value as T
        else -> factory(delegate.wrap(value, options))
    }
}
