package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageContext
import no.nav.hjelpemidler.kafka.KafkaMessage
import no.nav.hjelpemidler.serialization.jackson.jsonMapper

fun <T : KafkaMessage> MessageContext.publish(key: String, message: T) {
    publish(key, jsonMapper.writeValueAsString(message))
}
