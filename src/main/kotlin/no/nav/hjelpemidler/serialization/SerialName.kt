package no.nav.hjelpemidler.serialization

import kotlin.reflect.KClass

internal val KClass<*>.serialName: String
    get() = checkNotNull(qualifiedName) {
        "Kunne ikke utlede serialName, ${this::class} mangler qualifiedName"
    }
