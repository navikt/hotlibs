package no.nav.hjelpemidler.io

import java.io.InputStream
import kotlin.reflect.KClass

inline fun <T> Class<*>.useResourceAsStream(name: String, block: (InputStream) -> T): T =
    requireNotNull(getResourceAsStream(name)) { "Fant ikke resource: '$name'" }.use(block)

inline fun <T> KClass<*>.useResourceAsStream(name: String, block: (InputStream) -> T): T =
    java.useResourceAsStream(name, block)
