package no.nav.hjelpemidler.kafka

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class KafkaEvent(val name: String, vararg val alternativeNames: String) {
    companion object {
        const val NAME_KEY: String = "eventName"

        fun <T : KafkaMessage> from(messageClass: KClass<T>): KafkaEvent =
            messageClass.findAnnotation<KafkaEvent>() ?: error("'$messageClass' mangler KafkaEvent-annotasjon")
    }
}
