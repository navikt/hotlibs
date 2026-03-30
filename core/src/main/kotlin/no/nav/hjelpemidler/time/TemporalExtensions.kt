package no.nav.hjelpemidler.time

import java.time.DateTimeException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.Temporal
import kotlin.time.Duration
import kotlin.time.toJavaDuration

inline operator fun <reified T : Temporal> T.minus(duration: Duration): T =
    minus(duration.toJavaDuration()) as T

inline operator fun <reified T : Temporal> T.plus(duration: Duration): T =
    plus(duration.toJavaDuration()) as T

fun Temporal.asInstant(zoneId: ZoneId = ZONE_ID_EUROPE_OSLO): Instant = when (this) {
    is Instant -> this

    is LocalDateTime -> atZone(zoneId).toInstant()
    is OffsetDateTime -> toInstant()
    is ZonedDateTime -> toInstant()

    is LocalDate -> dateTimeError("Cannot convert LocalDate to Instant, call e.g. atStartOfDay() first")

    else -> dateTimeError("Unsupported temporal type: ${this::class.simpleName}")
}

fun Temporal.asLocalDate(zoneId: ZoneId = ZONE_ID_EUROPE_OSLO): LocalDate = when (this) {
    is LocalDate -> this
    is LocalDateTime -> toLocalDate()

    is Instant -> atZone(zoneId).toLocalDate()
    is OffsetDateTime -> atZoneSameInstant(zoneId).toLocalDate()
    is ZonedDateTime -> withZoneSameInstant(zoneId).toLocalDate()

    else -> dateTimeError("Unsupported temporal type: ${this::class.simpleName}")
}

fun Temporal.asLocalDateTime(zoneId: ZoneId = ZONE_ID_EUROPE_OSLO): LocalDateTime = when (this) {
    is LocalDateTime -> this

    is Instant -> atZone(zoneId).toLocalDateTime()
    is OffsetDateTime -> atZoneSameInstant(zoneId).toLocalDateTime()
    is ZonedDateTime -> withZoneSameInstant(zoneId).toLocalDateTime()

    is LocalDate -> dateTimeError("Cannot convert LocalDate to LocalDateTime, call e.g. atStartOfDay() first")

    else -> dateTimeError("Unsupported temporal type: ${this::class.simpleName}")
}

fun Temporal.asOffsetDateTime(zoneId: ZoneId = ZONE_ID_EUROPE_OSLO): OffsetDateTime = when (this) {
    is OffsetDateTime -> this

    is Instant -> atZone(zoneId).toOffsetDateTime()
    is LocalDateTime -> atZone(zoneId).toOffsetDateTime()
    is ZonedDateTime -> withZoneSameInstant(zoneId).toOffsetDateTime()

    is LocalDate -> dateTimeError("Cannot convert LocalDate to OffsetDateTime, call e.g. atStartOfDay() first")

    else -> dateTimeError("Unsupported temporal type: ${this::class.simpleName}")
}

fun Temporal.asZonedDateTime(zoneId: ZoneId = ZONE_ID_EUROPE_OSLO): ZonedDateTime = when (this) {
    is ZonedDateTime -> this

    is Instant -> atZone(zoneId)
    is LocalDateTime -> atZone(zoneId)
    is OffsetDateTime -> atZoneSameInstant(zoneId)

    is LocalDate -> dateTimeError("Cannot convert LocalDate to ZonedDateTime, call e.g. atStartOfDay() first")

    else -> dateTimeError("Unsupported temporal type: ${this::class.simpleName}")
}

private fun dateTimeError(message: String): Nothing = throw DateTimeException(message)
