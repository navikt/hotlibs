package no.nav.hjelpemidler.rapids_and_rivers

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.kafka.KafkaEvent
import no.nav.hjelpemidler.kafka.KafkaMessage
import java.util.UUID

@KafkaEvent(TestMessage.EVENT_NAME, TestMessage.ALTERNATIVE_EVENT_NAME)
data class TestMessage(
    val id: String,
    val vedtakId: String?,
    @param:JsonAlias("soknadId")
    val søknadId: UUID,
    @param:JsonProperty("fnrBruker")
    val brukerFnr: String,
    override val eventId: UUID = UUID.randomUUID(),
) : KafkaMessage {
    companion object {
        const val EVENT_NAME = "hm-test-kafka-event"
        const val ALTERNATIVE_EVENT_NAME = "hm-test-kafka-event-legacy"
    }
}
