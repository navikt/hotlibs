package no.nav.hjelpemidler.rapids_and_rivers

import com.fasterxml.jackson.databind.PropertyName
import com.github.navikt.tbd_libs.rapids_and_rivers.JsonMessage
import no.nav.hjelpemidler.collections.mapOfNotNull
import no.nav.hjelpemidler.kafka.Event
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.uuidValue
import java.util.UUID
import kotlin.reflect.KClass

fun jsonMessageOf(vararg pairs: Pair<String, Any?>): JsonMessage =
    JsonMessage.newMessage(mapOfNotNull(*pairs))

val JsonMessage.eventId: UUID get() = this["eventId"].uuidValue()
val JsonMessage.eventName: String get() = this["eventName"].textValue()

inline fun <reified T : Any> JsonMessage.value(): T = jsonToValue<T>(toJson())

fun <T : Event> JsonMessage.require(eventClass: KClass<T>) {
    val description = jsonMapper.deserializationConfig.run {
        introspect(constructType(eventClass.java))
    }

    description.findProperties()
        .forEach { property ->
            val aliases = property
                .findAliases()
                .map(PropertyName::getSimpleName)
                .toTypedArray()

            if (property.isRequired) {
                if (aliases.isEmpty()) {
                    requireKey(property.name)
                } else {
                    // todo -> her burde vi hatt en "requireAnyKey" i rapids-and-rivers
                    interestedIn(property.name, *aliases)
                }
            } else {
                interestedIn(property.name, *aliases)
            }
        }
}

inline fun <reified T : Event> JsonMessage.require() =
    require(T::class)
