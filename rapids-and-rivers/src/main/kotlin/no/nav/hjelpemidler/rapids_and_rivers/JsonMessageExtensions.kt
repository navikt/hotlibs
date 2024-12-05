package no.nav.hjelpemidler.rapids_and_rivers

import com.fasterxml.jackson.databind.JsonNode
import com.github.navikt.tbd_libs.rapids_and_rivers.JsonMessage
import no.nav.hjelpemidler.domain.id.UUID
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import java.util.UUID

fun JsonNode.uuidValue(): UUID = UUID(textValue())
fun JsonNode.uuidValueOrNull(): UUID? = textValue()?.let(::UUID)

fun JsonNode.fødselsnummerValue(): Fødselsnummer = Fødselsnummer(textValue())
fun JsonNode.fødselsnummerValueOrNull(): Fødselsnummer? = textValue()?.let(::Fødselsnummer)

val JsonMessage.eventId: UUID get() = this["eventId"].uuidValue()
val JsonMessage.eventName: String get() = this["eventName"].textValue()
