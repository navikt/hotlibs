package no.nav.hjelpemidler.rapids_and_rivers

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.kafka.KafkaEvent
import no.nav.hjelpemidler.kafka.KafkaEventName
import java.util.UUID

@KafkaEventName(TestKafkaEvent.EVENT_NAME)
data class TestKafkaEvent(
    val id: String,
    val vedtakId: String?,
    @JsonAlias("soknadId")
    val søknadId: UUID,
    @JsonProperty("fnrBruker")
    val brukerFnr: String,
    override val eventId: UUID = UUID.randomUUID(),
) : KafkaEvent {
    companion object {
        const val EVENT_NAME = "hm-test-kafka-event"
    }
}
