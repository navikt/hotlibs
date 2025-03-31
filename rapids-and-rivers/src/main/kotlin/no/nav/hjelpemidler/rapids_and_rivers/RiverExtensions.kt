package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.rapids_and_rivers.River
import no.nav.hjelpemidler.kafka.KafkaEvent
import no.nav.hjelpemidler.kafka.KafkaEventName
import no.nav.hjelpemidler.kafka.kafkaEventNameOf

inline fun <reified T : KafkaEvent> River.event(): River {
    val eventName = kafkaEventNameOf<T>()
    return this
        .precondition {
            if (eventName.alternatives.isEmpty()) {
                it.requireValue(KafkaEventName.KEY, eventName.value)
            } else {
                it.requireAny(KafkaEventName.KEY, listOf(eventName.value, *eventName.alternatives))
            }
        }
        .validate {
            it.require<T>()
        }
}
