package no.nav.hjelpemidler.kafka

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.InetAddress
import java.time.Instant
import java.util.UUID

/**
 * NB! Implementasjoner må annoteres med [KafkaEvent] eller overstyre [eventName].
 */
interface KafkaMessage {
    val eventId: UUID
    val eventName: String get() = KafkaEvent.of(javaClass).name
    val opprettet: Instant get() = Instant.now()

    val systemReadCount: Int
        @JsonProperty("system_read_count") get() = 0
    val systemParticipatingServices: List<ParticipatingService>
        @JsonProperty("system_participating_services") get() = listOf(
            ParticipatingService(id = eventId, time = opprettet)
        )

    companion object {
        const val EVENT_ID_KEY: String = "eventId"
        const val EVENT_NAME_KEY: String = KafkaEvent.NAME_KEY
    }

    data class ParticipatingService(
        val id: UUID,
        val time: Instant,
        val service: String? = System.getenv("NAIS_APP_NAME"),
        val instance: String? = InetAddress.getLocalHost().hostName,
        val image: String? = System.getenv("NAIS_APP_IMAGE"),
    )
}
