package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.rapids_and_rivers.River
import com.github.navikt.tbd_libs.rapids_and_rivers_api.RapidsConnection
import no.nav.hjelpemidler.kafka.KafkaMessage

inline fun <reified T : KafkaMessage> RapidsConnection.register(listener: KafkaMessageListener<T>): River =
    River(this).event<T>().register(listener)
