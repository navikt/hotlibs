package no.nav.hjelpemidler.service

import java.util.ServiceConfigurationError
import java.util.ServiceLoader
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

fun <S : Any> loadService(kClass: KClass<S>): Lazy<S> = lazy {
    val serviceName = kClass.qualifiedName ?: ""
    try {
        ServiceLoader
            .load(kClass.java)
            .minByOrNull { service ->
                service::class.findAnnotation<LoadOrder>()?.value ?: Integer.MAX_VALUE
            } ?: throw NoSuchElementException("Kunne ikke opprette tjeneste: $serviceName")
    } catch (e: ServiceConfigurationError) {
        throw RuntimeException("Kunne ikke opprette tjeneste: $serviceName", e)
    }
}

inline fun <reified S : Any> loadService() = loadService(S::class)
