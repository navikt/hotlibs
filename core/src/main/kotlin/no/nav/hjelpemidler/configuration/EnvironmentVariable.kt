package no.nav.hjelpemidler.configuration

import no.nav.hjelpemidler.function.identity
import no.nav.hjelpemidler.text.parseTo
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class EnvironmentVariable<out T>(
    private val prefix: String? = null,
    private val suffix: String? = null,
    private val defaultValue: T? = null,
    private val transform: (String) -> T,
) : ReadOnlyProperty<Any?, T> {
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val key = EnvironmentVariableKey(property, prefix, suffix)
        val value = Configuration[key]?.let(transform) ?: defaultValue
        check(property.returnType.isMarkedNullable || value != null) {
            "Miljøvariabelen '$key' mangler"
        }
        return value as T
    }

    companion object : PropertyDelegateProvider<Any?, ReadOnlyProperty<Any?, String>> {
        private val delegate: ReadOnlyProperty<Any?, String> = EnvironmentVariable(transform = ::identity)

        override fun provideDelegate(thisRef: Any?, property: KProperty<*>): ReadOnlyProperty<Any?, String> = delegate

        inline operator fun <reified T : Any> invoke(
            prefix: String? = null,
            suffix: String? = null,
            defaultValue: T? = null,
        ): ReadOnlyProperty<Any?, T> = EnvironmentVariable(prefix, suffix, defaultValue) { rawValue ->
            rawValue.parseTo<T>()
        }
    }
}
