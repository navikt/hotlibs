package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.domain.ValueType
import org.hibernate.dialect.Dialect
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.type.descriptor.java.AbstractClassJavaType
import org.hibernate.type.descriptor.java.BasicJavaType
import org.hibernate.type.descriptor.java.JavaType.CoercionContext
import org.hibernate.type.descriptor.jdbc.JdbcType
import org.hibernate.type.descriptor.jdbc.JdbcTypeIndicators
import kotlin.reflect.KClass

abstract class ValueJavaType<T : ValueType<R>, R : Any>(
    type: KClass<T>,
    protected val delegate: BasicJavaType<R>,
    protected val factory: (R) -> T,
) : AbstractClassJavaType<T>(type.java) {
    override fun getDefaultSqlLength(dialect: Dialect, jdbcType: JdbcType): Long =
        delegate.getDefaultSqlLength(dialect, jdbcType)

    override fun getDefaultSqlPrecision(dialect: Dialect, jdbcType: JdbcType): Int =
        delegate.getDefaultSqlPrecision(dialect, jdbcType)

    override fun getDefaultSqlScale(dialect: Dialect, jdbcType: JdbcType): Int =
        delegate.getDefaultSqlScale(dialect, jdbcType)

    override fun getRecommendedJdbcType(indicators: JdbcTypeIndicators): JdbcType? =
        delegate.getRecommendedJdbcType(indicators)

    override fun useObjectEqualsHashCode(): Boolean = true

    override fun toString(value: T?): String? = value?.toString()

    override fun fromString(string: CharSequence?): T? = delegate.fromString(string)?.let { factory(it) }

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

    override fun <X : Any> coerce(value: X?, coercionContext: CoercionContext): T? =
        delegate.coerce(value, coercionContext)?.let { factory(it) }
}
