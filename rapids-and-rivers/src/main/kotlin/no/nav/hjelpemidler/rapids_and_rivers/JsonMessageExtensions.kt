package no.nav.hjelpemidler.rapids_and_rivers

import com.fasterxml.jackson.databind.PropertyName
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.github.navikt.tbd_libs.rapids_and_rivers.JsonMessage
import no.nav.hjelpemidler.collections.filterNotNull
import no.nav.hjelpemidler.collections.mapOfNotNull
import no.nav.hjelpemidler.kafka.KafkaMessage
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.uuidValue
import java.util.UUID

fun jsonMessageOf(map: Map<String, Any?>): JsonMessage =
    JsonMessage.newMessage(map.filterNotNull())

fun jsonMessageOf(vararg pairs: Pair<String, Any?>): JsonMessage =
    jsonMessageOf(mapOfNotNull(*pairs))

val JsonMessage.eventId: UUID get() = this[KafkaMessage.EVENT_ID_KEY].uuidValue()
val JsonMessage.eventName: String get() = this[KafkaMessage.EVENT_NAME_KEY].textValue()

/**
 * Gjør om JSON i melding til instans av klasse [T].
 */
inline fun <reified T : Any> JsonMessage.value(): T = jsonToValue<T>(toJson())

/**
 * Finn ut hvilke felter som kreves og hvilke vi er interessert i basert på type [T].
 * Implementasjonen baserer seg på [com.fasterxml.jackson.databind.BeanDescription] fra `jackson-databind`.
 * Annotasjonene [com.fasterxml.jackson.annotation.JsonAlias] og [com.fasterxml.jackson.annotation.JsonProperty]
 * blir tatt høyde for.
 *
 * NB! Felter fra nestede objekter blir ikke håndtert pt. Kun nøkkelen for objektet blir registrert.
 *
 * @see [JsonMessage.requireKey]
 * @see [JsonMessage.interestedIn]
 * @see [com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition.isRequired]
 */
inline fun <reified T : KafkaMessage> JsonMessage.require() {
    requireKey(KafkaMessage.EVENT_ID_KEY, KafkaMessage.EVENT_NAME_KEY)

    val description = jsonMapper.deserializationConfig.run {
        introspect(constructType(jacksonTypeRef<T>()))
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
