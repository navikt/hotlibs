package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.rapids_and_rivers.River
import no.nav.hjelpemidler.kafka.Event
import no.nav.hjelpemidler.kafka.EventName
import no.nav.hjelpemidler.kafka.eventNameOf
import kotlin.reflect.KClass

fun <T : Event> River.event(eventClass: KClass<T>): River {
    val eventName = eventNameOf(eventClass)
    return this
        .precondition {
            if (eventName.alternatives.isEmpty()) {
                it.requireValue(EventName.KEY, eventName.value)
            } else {
                it.requireAny(EventName.KEY, listOf(eventName.value, *eventName.alternatives))
            }
        }
        .validate {
            it.require(eventClass)
        }
}

inline fun <reified T : Event> River.event(): River =
    event(T::class)
