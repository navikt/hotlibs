package no.nav.hjelpemidler.configuration

import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

inline fun <reified T : Any> environmentVariablesIn(value: T): List<String> =
    T::class.declaredMemberProperties.mapNotNull { property ->
        property.isAccessible = true
        property.getDelegate(value)
        when (property.getDelegate(value)) {
            is EnvironmentVariable -> property.name
            else -> null
        }
    }
