package no.nav.hjelpemidler.kafka

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class KafkaEvent(val name: String, vararg val alternativeNames: String) {
    companion object {
        const val KEY: String = "eventName"

        fun <T : KafkaMessage> from(eventClass: KClass<T>): KafkaEvent =
            eventClass.findAnnotation<KafkaEvent>() ?: error("'$eventClass' mangler KafkaEvent-annotasjon")
    }
}
