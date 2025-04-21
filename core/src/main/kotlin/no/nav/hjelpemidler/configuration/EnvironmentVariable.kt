package no.nav.hjelpemidler.configuration

import java.net.URI
import java.net.URL
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

val EnvironmentVariable: ReadOnlyProperty<Any?, String> = ReadOnlyProperty { _, property ->
    checkNotNull(Configuration[property.name]) {
        "Miljøvariabelen '${property.name}' mangler"
    }
}

inline fun <reified T> environmentVariable(prefix: String? = null): ReadOnlyProperty<Any?, T> =
    ReadOnlyProperty { _, property ->
        val returnType = property.returnType
        val propertyName = if (prefix == null) property.name else "${prefix}_${property.name}"
        val variable = Configuration[propertyName]
        val value = when {
            returnType.isSubtypeOf(typeOf<Boolean?>()) -> variable?.toBoolean()
            returnType.isSubtypeOf(typeOf<Int?>()) -> variable?.toInt()
            returnType.isSubtypeOf(typeOf<String?>()) -> variable
            returnType.isSubtypeOf(typeOf<URI?>()) -> variable?.let(URI::create)
            returnType.isSubtypeOf(typeOf<URL?>()) -> variable?.let(URI::create)?.toURL()
            else -> throw UnsupportedOperationException("Mangler støtte for type: $returnType")
        }
        check(returnType.isMarkedNullable || value != null) {
            "Miljøvariabelen '$propertyName' mangler"
        }
        value as T
    }
