package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.UUID

val JsonNode?.isMissingOrNull: Boolean get() = this == null || isMissingNode || isNull

inline fun <T : Any> JsonNode?.ifText(transform: (String) -> T): T? = this?.textValue()?.let(transform)

fun JsonNode.uuidValue(): UUID = UUID.fromString(textValue())
fun JsonNode?.uuidValueOrNull(): UUID? = ifText(UUID::fromString)

fun JsonNode.localDateValue(): LocalDate = LocalDate.parse(textValue())
fun JsonNode?.localDateValueOrNull(): LocalDate? = ifText(LocalDate::parse)

fun JsonNode.localDateTimeValue(): LocalDateTime = LocalDateTime.parse(textValue())
fun JsonNode?.localDateTimeValueOrNull(): LocalDateTime? = ifText(LocalDateTime::parse)

fun JsonNode.instantValue(): Instant = Instant.parse(textValue())
fun JsonNode?.instantValueOrNull(): Instant? = ifText(Instant::parse)

fun JsonNode.zonedDateTime(): ZonedDateTime = ZonedDateTime.parse(textValue())
fun JsonNode?.zonedDateTimeOrNull(): ZonedDateTime? = ifText(ZonedDateTime::parse)

inline fun <reified E : Enum<E>> JsonNode.enumValue(): E = enumValueOf<E>(textValue())
inline fun <reified E : Enum<E>> JsonNode?.enumValueOrNull(): E? = ifText(::enumValueOf)

fun JsonNode.fødselsnummerValue(): Fødselsnummer = Fødselsnummer(textValue())
fun JsonNode?.fødselsnummerValueOrNull(): Fødselsnummer? = ifText(::Fødselsnummer)

inline fun <reified T : Any> JsonNode.value(): T = treeToValue<T>(this)
inline fun <reified T : Any> JsonNode?.valueOrNull(): T? = treeToValueOrNull<T>(this)

fun JsonNode.orNull(): JsonNode? = takeUnless(JsonNode::isMissingOrNull)
