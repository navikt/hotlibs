package no.nav.hjelpemidler.time

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date
import java.util.TimeZone
import kotlin.time.Duration

val TIME_ZONE_EUROPE_OSLO: TimeZone = TimeZone.getTimeZone("Europe/Oslo")
val ZONE_ID_EUROPE_OSLO: ZoneId = TIME_ZONE_EUROPE_OSLO.toZoneId()

fun nå(): Instant = Instant.now()
fun iDag(): LocalDate = LocalDate.now()

fun nowWithOffset(): OffsetDateTime = OffsetDateTime.now(ZONE_ID_EUROPE_OSLO)
fun nowWithTimeZone(): ZonedDateTime = ZonedDateTime.now(ZONE_ID_EUROPE_OSLO)

// Date
fun Date.toLocalDate(): LocalDate = toInstant().toLocalDate()
fun Date.toLocalDateTime(): LocalDateTime = toInstant().toLocalDateTime()

// LocalDate
fun LocalDate.toInstant(): Instant = atStartOfDay().atZone(ZONE_ID_EUROPE_OSLO).toInstant()
fun LocalDate.toDate(): Date = toInstant().toDate()
infix fun LocalDate.erISammeÅrSom(other: LocalDate): Boolean = year == other.year

// LocalDateTime
fun LocalDateTime.toInstant(): Instant = atZone(ZONE_ID_EUROPE_OSLO).toInstant()
fun LocalDateTime.toDate(): Date = toInstant().toDate()

// Instant
fun Instant.toLocalDate(): LocalDate = ZonedDateTime.ofInstant(this, ZONE_ID_EUROPE_OSLO).toLocalDate()
fun Instant.toLocalDateTime(): LocalDateTime = ZonedDateTime.ofInstant(this, ZONE_ID_EUROPE_OSLO).toLocalDateTime()
fun Instant.toZonedDateTime(): ZonedDateTime = atZone(ZONE_ID_EUROPE_OSLO)
fun Instant.toDate(): Date = Date.from(this)
operator fun Instant.minus(duration: Duration): Instant = minusNanos(duration.inWholeNanoseconds)
operator fun Instant.plus(duration: Duration): Instant = plusNanos(duration.inWholeNanoseconds)
