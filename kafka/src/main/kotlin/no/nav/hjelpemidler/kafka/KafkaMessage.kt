package no.nav.hjelpemidler.kafka

import java.util.UUID

/**
 * NB! Implementasjoner må annoteres med [KafkaEvent] eller overstyre [eventName].
 */
interface KafkaMessage {
    val eventId: UUID
    val eventName: String get() = KafkaEvent.from(this::class).name

    companion object {
        const val EVENT_ID_KEY: String = "eventId"
        const val EVENT_NAME_KEY: String = KafkaEvent.NAME_KEY
    }
}
