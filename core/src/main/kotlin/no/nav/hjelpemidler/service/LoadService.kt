package no.nav.hjelpemidler.service

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.ServiceConfigurationError
import java.util.ServiceLoader
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

private val log = KotlinLogging.logger {}

/**
 * Last tjeneste med [ServiceLoader]. Ved flere implementasjoner på classpath, vil den med laveste [LoadOrder] ha presedens.
 */
fun <S : Any> loadService(kClass: KClass<S>): Lazy<S> = lazy {
    val serviceName = kClass.qualifiedName ?: ""
    try {
        val service = ServiceLoader
            .load(kClass.java)
            .minByOrNull { service ->
                service::class.findAnnotation<LoadOrder>()?.value ?: Integer.MAX_VALUE
            } ?: throw NoSuchElementException("Kunne ikke opprette tjeneste: $serviceName")
        log.info { "Lastet tjeneste: $serviceName, implementasjon: ${service::class.qualifiedName}" }
        service
    } catch (e: ServiceConfigurationError) {
        throw RuntimeException("Kunne ikke opprette tjeneste: $serviceName", e)
    }
}

inline fun <reified S : Any> loadService() = loadService(S::class)
