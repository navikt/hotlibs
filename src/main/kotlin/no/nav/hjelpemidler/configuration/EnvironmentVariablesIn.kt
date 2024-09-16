package no.nav.hjelpemidler.configuration

import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

inline fun <reified T : Any> environmentVariablesIn(value: T, includeExternal: Boolean = false): List<String> =
    T::class.declaredMemberProperties
        .onEach { property -> property.isAccessible = true }
        .filter { property -> property.getDelegate(value) == EnvironmentVariable }
        .filter { property -> includeExternal || !property.hasAnnotation<External>() }
        .map { it.name }
