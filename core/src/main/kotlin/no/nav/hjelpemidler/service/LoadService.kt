package no.nav.hjelpemidler.service

import java.util.ServiceConfigurationError
import java.util.ServiceLoader
import kotlin.reflect.full.findAnnotation

/**
 * Last tjeneste med [ServiceLoader]. Ved flere implementasjoner på classpath, vil den med laveste [LoadOrder] ha presedens.
 */
inline fun <reified S : Any> loadService(): Lazy<S> = lazy {
    val type = S::class.java
    try {
        val service = ServiceLoader
            .load(type, type.classLoader)
            .iterator()
            .asSequence()
            .minByOrNull { service ->
                service::class.findAnnotation<LoadOrder>()?.value ?: Integer.MAX_VALUE
            } ?: throw IllegalStateException("Kunne ikke opprette tjeneste: ${type.name}")
        service
    } catch (e: ServiceConfigurationError) {
        throw IllegalStateException("Kunne ikke opprette tjeneste: ${type.name}", e)
    }
}
