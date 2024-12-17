package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import java.util.UUID

fun JsonNode.uuidValue(): UUID = UUID.fromString(textValue())
fun JsonNode.uuidValueOrNull(): UUID? = textValue()?.let(UUID::fromString)

fun JsonNode.fødselsnummerValue(): Fødselsnummer = Fødselsnummer(textValue())
fun JsonNode.fødselsnummerValueOrNull(): Fødselsnummer? = textValue()?.let(::Fødselsnummer)
