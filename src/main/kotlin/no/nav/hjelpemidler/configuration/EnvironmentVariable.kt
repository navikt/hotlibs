package no.nav.hjelpemidler.configuration

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.typeOf

val EnvironmentVariable: ReadOnlyProperty<Any?, String> = ReadOnlyProperty { _, property ->
    checkNotNull(Configuration.current[property.name]) {
        "Miljøvariabelen '${property.name}' mangler"
    }
}

inline fun <reified T> environmentVariable(prefix: String? = null): ReadOnlyProperty<Any?, T> =
    ReadOnlyProperty { _, property ->
        val returnType = property.returnType
        val propertyName = if (prefix == null) property.name else "${prefix}_${property.name}"
        val variable = Configuration.current[propertyName]
        val value = when {
            returnType.isSubtypeOf(typeOf<String?>()) -> variable
            returnType.isSubtypeOf(typeOf<Int?>()) -> variable?.toInt()
            returnType.isSubtypeOf(typeOf<Boolean?>()) -> variable?.toBoolean()
            else -> throw UnsupportedOperationException("Mangler støtte for type: $returnType")
        }
        check(returnType.isMarkedNullable || value != null) {
            "Miljøvariabelen '$propertyName' mangler"
        }
        value as T
    }
