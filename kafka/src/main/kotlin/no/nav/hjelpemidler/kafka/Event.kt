package no.nav.hjelpemidler.kafka

import java.util.UUID

interface Event {
    val eventId: UUID
    val eventName: String get() = eventNameOf(this::class).value
}
