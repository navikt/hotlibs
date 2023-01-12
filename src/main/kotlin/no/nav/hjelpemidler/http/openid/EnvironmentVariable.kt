package no.nav.hjelpemidler.http.openid

import kotlin.reflect.KProperty

internal object EnvironmentVariable {
    operator fun get(name: String): String? = System.getenv(name)
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String =
        checkNotNull(this[property.name]) {
            "Miljøvariabelen '${property.name}' mangler"
        }
}
