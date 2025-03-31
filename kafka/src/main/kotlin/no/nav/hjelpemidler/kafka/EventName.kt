package no.nav.hjelpemidler.kafka

import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class EventName(val value: String, vararg val alternatives: String) {
    companion object {
        const val KEY = "eventName"
    }
}

fun <T : Event> eventNameOf(eventClass: KClass<T>): EventName =
    eventClass.findAnnotation<EventName>() ?: error("'$eventClass' mangler EventName-annotasjon")

inline fun <reified T : Event> eventNameOf(): EventName =
    eventNameOf(T::class)
