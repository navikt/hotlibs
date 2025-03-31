package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.rapids_and_rivers.River
import com.github.navikt.tbd_libs.rapids_and_rivers_api.RapidsConnection
import no.nav.hjelpemidler.kafka.Event

inline fun <reified T : Event> RapidsConnection.register(listener: EventListener<T>): River =
    River(this).event<T>().register(listener)
