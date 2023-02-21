package no.nav.hjelpemidler.configuration

import kotlin.reflect.KProperty

object EnvironmentVariable {
    private val fallback by Configuration.load()

    operator fun get(name: String): String? =
        System.getenv(name) ?: fallback[name]

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        checkNotNull(this[property.name]) {
            "Miljøvariabelen '${property.name}' mangler"
        }
}
