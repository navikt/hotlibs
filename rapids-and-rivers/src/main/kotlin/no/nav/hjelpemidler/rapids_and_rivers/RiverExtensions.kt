package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.rapids_and_rivers.River
import no.nav.hjelpemidler.kafka.KafkaEvent
import no.nav.hjelpemidler.kafka.KafkaMessage

/**
 * Konfigurer [River.precondition] og [River.validate] basert på klasse annotert med [KafkaEvent].
 *
 * @see [KafkaEvent]
 * @see [KafkaMessage]
 */
inline fun <reified T : KafkaMessage> River.event(): River {
    val event = KafkaEvent.from(T::class)
    return this
        .precondition {
            if (event.alternativeNames.isEmpty()) {
                it.requireValue(KafkaEvent.NAME_KEY, event.name)
            } else {
                it.requireAny(KafkaEvent.NAME_KEY, listOf(event.name, *event.alternativeNames))
            }
        }
        .validate {
            it.require<T>()
        }
}
