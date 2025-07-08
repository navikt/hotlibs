package no.nav.hjelpemidler.configuration

import no.nav.hjelpemidler.function.identity
import no.nav.hjelpemidler.text.toURI
import no.nav.hjelpemidler.text.toURL
import no.nav.hjelpemidler.text.toUUID
import java.net.URI
import java.net.URL
import java.util.UUID
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class EnvironmentVariable<out T> private constructor(
    private val prefix: String? = null,
    private val suffix: String? = null,
    private val defaultValue: T? = null,
    private val transform: (String) -> T,
) : ReadOnlyProperty<Any?, T> {
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val key = EnvironmentVariableKey(property, prefix, suffix)
        val value = if (defaultValue == null) {
            Configuration.get(key, transform)
        } else {
            Configuration.getOrDefault(key, defaultValue, transform)
        }
        check(property.returnType.isMarkedNullable || value != null) {
            "Miljøvariabelen '$key' mangler"
        }
        return value as T
    }

    companion object : PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, String>> {
        private val delegate: ReadOnlyProperty<Any?, String> = string()

        override fun provideDelegate(thisRef: Any?, property: KProperty<*>): ReadOnlyProperty<Any?, String> = delegate

        fun string(
            prefix: String? = null,
            suffix: String? = null,
            defaultValue: String? = null,
        ): ReadOnlyProperty<Any?, String> =
            EnvironmentVariable(prefix, suffix, defaultValue, transform = ::identity)

        fun boolean(
            prefix: String? = null,
            suffix: String? = null,
            defaultValue: Boolean? = null,
        ): ReadOnlyProperty<Any?, Boolean> =
            EnvironmentVariable(prefix, suffix, defaultValue, transform = String::toBoolean)

        fun int(
            prefix: String? = null,
            suffix: String? = null,
            defaultValue: Int? = null,
        ): ReadOnlyProperty<Any?, Int> =
            EnvironmentVariable(prefix, suffix, defaultValue, transform = String::toInt)

        fun double(
            prefix: String? = null,
            suffix: String? = null,
            defaultValue: Double? = null,
        ): ReadOnlyProperty<Any?, Double> =
            EnvironmentVariable(prefix, suffix, defaultValue, transform = String::toDouble)

        fun uri(
            prefix: String? = null,
            suffix: String? = null,
            defaultValue: URI? = null,
        ): ReadOnlyProperty<Any?, URI> =
            EnvironmentVariable(prefix, suffix, defaultValue, transform = String::toURI)

        fun url(
            prefix: String? = null,
            suffix: String? = null,
            defaultValue: URL? = null,
        ): ReadOnlyProperty<Any?, URL> =
            EnvironmentVariable(prefix, suffix, defaultValue, transform = String::toURL)

        fun uuid(
            prefix: String? = null,
            suffix: String? = null,
            defaultValue: UUID? = null,
        ): ReadOnlyProperty<Any?, UUID> =
            EnvironmentVariable(prefix, suffix, defaultValue, transform = String::toUUID)
    }
}
