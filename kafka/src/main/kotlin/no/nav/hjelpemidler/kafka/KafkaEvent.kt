package no.nav.hjelpemidler.kafka

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class KafkaEvent(
    /**
     * `eventName` som skal brukes i melding.
     */
    val name: String,
    /**
     * Andre `eventName` for melding, f.eks. tidligere brukte verdier.
     */
    vararg val alternativeNames: String,
) {
    companion object {
        const val NAME_KEY: String = "eventName"

        fun <T : KafkaMessage> from(messageClass: KClass<T>): KafkaEvent = eventByClass.computeIfAbsent(messageClass) {
            it.findAnnotation<KafkaEvent>() ?: error("'$it' mangler KafkaEvent-annotasjon")
        }

        private val eventByClass: MutableMap<KClass<*>, KafkaEvent> = ConcurrentHashMap()
    }
}
