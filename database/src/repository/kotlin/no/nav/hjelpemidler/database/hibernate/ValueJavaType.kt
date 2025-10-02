package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.domain.ValueType
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.type.descriptor.java.AbstractClassJavaType
import org.hibernate.type.descriptor.java.BasicJavaType
import org.hibernate.type.descriptor.jdbc.JdbcType
import org.hibernate.type.descriptor.jdbc.JdbcTypeIndicators
import kotlin.reflect.KClass

abstract class ValueJavaType<T : ValueType<R>, R : Any>(
    type: KClass<T>,
    private val delegate: BasicJavaType<R>,
    private val factory: (R) -> T,
) : AbstractClassJavaType<T>(type.java) {
    override fun getRecommendedJdbcType(indicators: JdbcTypeIndicators): JdbcType? =
        delegate.getRecommendedJdbcType(indicators)

    override fun useObjectEqualsHashCode(): Boolean = true

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
