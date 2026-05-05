package no.nav.hjelpemidler.kafka

import java.util.UUID

data class BigQueryHendelse(
    val schemaId: String,
    val payload: Map<String, Any?>,
    override val eventId: UUID = UUID.randomUUID(),
) : KafkaMessage {
    override val eventName: String = EVENT_NAME

    companion object {
        const val EVENT_NAME = "hm-bigquery-sink-hendelse"
    }
}
