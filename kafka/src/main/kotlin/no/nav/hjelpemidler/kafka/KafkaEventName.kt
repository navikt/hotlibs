package no.nav.hjelpemidler.kafka

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class KafkaEventName(val value: String, vararg val alternatives: String) {
    companion object {
        const val KEY = "eventName"
    }
}

fun <T : KafkaEvent> kafkaEventNameOf(eventClass: KClass<T>): KafkaEventName =
    eventClass.findAnnotation<KafkaEventName>() ?: error("'$eventClass' mangler KafkaEventName-annotasjon")

inline fun <reified T : KafkaEvent> kafkaEventNameOf(): KafkaEventName =
    kafkaEventNameOf(T::class)
