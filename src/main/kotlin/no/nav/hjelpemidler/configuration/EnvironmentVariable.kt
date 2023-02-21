package no.nav.hjelpemidler.configuration

import kotlin.reflect.KProperty

object EnvironmentVariable {
    operator fun get(name: String): String? =
        Configuration.current[name]

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        checkNotNull(this[property.name]) {
            "Miljøvariabelen '${property.name}' mangler"
        }
}
