package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageContext
import no.nav.hjelpemidler.kafka.KafkaMessage
import no.nav.hjelpemidler.serialization.jackson.jsonMapper

class ExtendedMessageContext(private val messageContext: MessageContext) : MessageContext by messageContext {
    fun <T : KafkaMessage> publish(key: String, message: T) =
        publish(key, jsonMapper.writeValueAsString(message))
}
