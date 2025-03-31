package no.nav.hjelpemidler.kafka

import java.util.UUID

interface KafkaMessage {
    val eventId: UUID
    val eventName: String get() = KafkaEvent.from(this::class).name

    companion object {
        const val EVENT_ID_KEY: String = "eventId"
        const val EVENT_NAME_KEY: String = KafkaEvent.KEY
    }
}
