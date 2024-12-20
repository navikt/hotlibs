package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.treeToValue
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import java.util.UUID

fun JsonNode.uuidValue(): UUID = UUID.fromString(textValue())
fun JsonNode.uuidValueOrNull(): UUID? = textValue()?.let(UUID::fromString)

inline fun <reified E : Enum<E>> JsonNode.enumValue(): E = enumValueOf<E>(textValue())
inline fun <reified E : Enum<E>> JsonNode.enumValueOrNull(): E? = textValue()?.let { enumValueOf<E>(it) }

fun JsonNode.fødselsnummerValue(): Fødselsnummer = Fødselsnummer(textValue())
fun JsonNode.fødselsnummerValueOrNull(): Fødselsnummer? = textValue()?.let(::Fødselsnummer)

inline fun <reified T : Any> JsonNode.value(): T = jsonMapper.treeToValue<T>(this)
inline fun <reified T : Any> JsonNode?.valueOrNull(): T? = this?.let<JsonNode, T>(jsonMapper::treeToValue)
