package no.nav.hjelpemidler.kafka

import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.configuration.NaisEnvironmentVariable
import java.net.InetAddress
import java.time.Instant
import java.util.UUID

data class KafkaStandardFields(
    @JsonProperty("@id")
    val eventId: UUID,
    @JsonProperty("@event_name")
    val eventName: String,
    @JsonProperty("@opprettet")
    val opprettet: Instant = Instant.now(),
    @JsonProperty("system_read_count")
    val systemReadCount: Int = 0,
    @JsonProperty("system_participating_services")
    val systemParticipatingServices: List<ParticipatingService> = listOf(
        ParticipatingService(
            id = eventId,
            time = opprettet,
        )
    ),
) {
    data class ParticipatingService(
        val id: UUID,
        val time: Instant,
        val service: String = NaisEnvironmentVariable.NAIS_APP_NAME,
        val instance: String = InetAddress.getLocalHost().hostName,
        val image: String = NaisEnvironmentVariable.NAIS_APP_IMAGE,
    )
}
