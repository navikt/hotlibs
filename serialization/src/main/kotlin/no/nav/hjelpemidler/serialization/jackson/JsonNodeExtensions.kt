package no.nav.hjelpemidler.serialization.jackson

import no.nav.hjelpemidler.domain.person.Fødselsnummer
import tools.jackson.databind.JsonNode
import tools.jackson.databind.node.NumericIntNode
import tools.jackson.databind.node.StringNode
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.UUID

val JsonNode?.isMissingOrNull: Boolean get() = this == null || isMissingNode || isNull

inline fun <reified T : JsonNode, R : Any> JsonNode?.ifNode(transform: (T) -> R): R? =
    takeIf { this is T }?.let { transform(it as T) }

inline fun <T : Any> JsonNode?.ifStringNode(transform: (String) -> T): T? =
    takeIf { this is StringNode }?.let { transform(it.stringValue()) }

fun JsonNode.intValueOrNull(): Int? = takeIf { this is NumericIntNode }?.intValue()
fun JsonNode.longValueOrNull(): Long? = takeIf { this is NumericIntNode }?.longValue()
fun JsonNode.stringValueOrNull(): String? = takeIf { this is StringNode }?.stringValue()

fun JsonNode.uuidValue(): UUID = UUID.fromString(stringValue())
fun JsonNode?.uuidValueOrNull(): UUID? = ifStringNode(UUID::fromString)

fun JsonNode.localDateValue(): LocalDate = LocalDate.parse(stringValue())
fun JsonNode?.localDateValueOrNull(): LocalDate? = ifStringNode(LocalDate::parse)

fun JsonNode.localDateTimeValue(): LocalDateTime = LocalDateTime.parse(stringValue())
fun JsonNode?.localDateTimeValueOrNull(): LocalDateTime? = ifStringNode(LocalDateTime::parse)

fun JsonNode.instantValue(): Instant = Instant.parse(stringValue())
fun JsonNode?.instantValueOrNull(): Instant? = ifStringNode(Instant::parse)

fun JsonNode.zonedDateTime(): ZonedDateTime = ZonedDateTime.parse(stringValue())
fun JsonNode?.zonedDateTimeOrNull(): ZonedDateTime? = ifStringNode(ZonedDateTime::parse)

inline fun <reified E : Enum<E>> JsonNode.enumValue(): E = enumValueOf<E>(stringValue())
inline fun <reified E : Enum<E>> JsonNode?.enumValueOrNull(): E? = ifStringNode(::enumValueOf)

fun JsonNode.fødselsnummerValue(): Fødselsnummer = Fødselsnummer(stringValue())
fun JsonNode?.fødselsnummerValueOrNull(): Fødselsnummer? = ifStringNode(::Fødselsnummer)

inline fun <reified T : Any> JsonNode.value(): T = treeToValue<T>(this)
inline fun <reified T : Any> JsonNode?.valueOrNull(): T? = treeToValueOrNull<T>(this)

fun JsonNode.orNull(): JsonNode? = takeUnless(JsonNode::isMissingOrNull)
