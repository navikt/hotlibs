package no.nav.hjelpemidler.time

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.toJavaDuration

// START Instant

fun Instant.toLocalDate(zoneId: ZoneId = ZONE_ID_EUROPE_OSLO): LocalDate = atZone(zoneId).toLocalDate()
fun Instant.toLocalDateTime(zoneId: ZoneId = ZONE_ID_EUROPE_OSLO): LocalDateTime = atZone(zoneId).toLocalDateTime()
fun Instant.toOffsetDateTime(zoneId: ZoneId = ZONE_ID_EUROPE_OSLO): OffsetDateTime = atZone(zoneId).toOffsetDateTime()
fun Instant.toZonedDateTime(zoneId: ZoneId = ZONE_ID_EUROPE_OSLO): ZonedDateTime = atZone(zoneId)

infix operator fun Instant.plus(duration: Duration): Instant = plus(duration.toJavaDuration())
infix operator fun Instant.minus(duration: Duration): Instant = minus(duration.toJavaDuration())

// END Instant

// START LocalDateTime

fun LocalDateTime.toInstant(zoneId: ZoneId = ZONE_ID_EUROPE_OSLO): Instant = atZone(zoneId).toInstant()
fun LocalDateTime.toOffsetDateTime(zoneId: ZoneId = ZONE_ID_EUROPE_OSLO): OffsetDateTime = atZone(zoneId).toOffsetDateTime()
fun LocalDateTime.toZonedDateTime(zoneId: ZoneId = ZONE_ID_EUROPE_OSLO): ZonedDateTime = atZone(zoneId)

infix operator fun LocalDateTime.plus(duration: Duration): LocalDateTime = plus(duration.toJavaDuration())
infix operator fun LocalDateTime.minus(duration: Duration): LocalDateTime = minus(duration.toJavaDuration())

// END LocalDateTime
