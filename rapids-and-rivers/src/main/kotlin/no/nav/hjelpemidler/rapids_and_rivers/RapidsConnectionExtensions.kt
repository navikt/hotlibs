package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.rapids_and_rivers.River
import com.github.navikt.tbd_libs.rapids_and_rivers_api.RapidsConnection
import no.nav.hjelpemidler.kafka.KafkaEvent

inline fun <reified T : KafkaEvent> RapidsConnection.register(listener: KafkaEventListener<T>): River =
    River(this).event<T>().register(listener)
