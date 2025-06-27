package no.nav.hjelpemidler.configuration

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class EnvironmentVariable<T>(
    private val prefix: String? = null,
    private val defaultValue: T? = null,
    private val transform: (String) -> T,
) : ReadOnlyProperty<Any?, T> {
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val propertyName = if (prefix == null) property.name else "${prefix}_${property.name}"
        val rawValue = Configuration[propertyName]
        val value = if (rawValue == null) defaultValue else transform(rawValue)
        check(property.returnType.isMarkedNullable || value != null) {
            "Miljøvariabelen '$propertyName' mangler"
        }
        return value as T
    }

    companion object : ReadOnlyProperty<Any?, String> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): String =
            checkNotNull(Configuration[property.name]) {
                "Miljøvariabelen '${property.name}' mangler"
            }
    }
}
