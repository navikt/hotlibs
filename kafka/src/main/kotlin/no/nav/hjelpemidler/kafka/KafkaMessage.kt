package no.nav.hjelpemidler.kafka

import com.fasterxml.jackson.annotation.JsonUnwrapped
import java.util.UUID

/**
 * NB! Implementasjoner må annoteres med [KafkaEvent] eller overstyre [eventName].
 */
interface KafkaMessage {
    val eventId: UUID
    val eventName: String get() = KafkaEvent.of(javaClass).name

    /**
     * Felter som legges til automatisk med `com.github.navikt.tbd_libs.rapids_and_rivers.JsonMessage` og som brukes til sporing.
     */
    @Suppress("unused")
    val standardFields: KafkaStandardFields
        @JsonUnwrapped get() = KafkaStandardFields(
            eventId = eventId,
            eventName = eventName,
        )

    companion object {
        const val EVENT_ID_KEY: String = "eventId"
        const val EVENT_NAME_KEY: String = KafkaEvent.NAME_KEY
    }
}
