package no.nav.hjelpemidler.kafka

import java.util.UUID

interface KafkaEvent {
    val eventId: UUID
    val eventName: String get() = kafkaEventNameOf(this::class).value
}
