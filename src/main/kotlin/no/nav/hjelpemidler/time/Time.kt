package no.nav.hjelpemidler.time

import no.bekk.bekkopen.date.NorwegianDateUtil
import no.nav.hjelpemidler.localization.LOCALE_NORWAY
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.TimeZone
import kotlin.time.Duration

fun nå(): Instant =
    Instant.now()

fun iDag(): LocalDate =
    LocalDate.now()

fun Date.toLocalDate(): LocalDate =
    toInstant().toLocalDate()

fun Date.toLocalDateTime(): LocalDateTime =
    toInstant().toLocalDateTime()

fun LocalDate.toInstant(): Instant =
    atStartOfDay().atZone(ZONE_ID_EUROPE_OSLO).toInstant()

fun LocalDate.toDate(): Date =
    toInstant().toDate()

infix fun LocalDate.erISammeÅrSom(other: LocalDate): Boolean =
    year == other.year

fun LocalDateTime.toInstant(): Instant =
    atZone(ZONE_ID_EUROPE_OSLO).toInstant()

fun LocalDateTime.toDate(): Date =
    toInstant().toDate()

fun Instant.toLocalDate(): LocalDate =
    ZonedDateTime.ofInstant(this, ZONE_ID_EUROPE_OSLO).toLocalDate()

fun Instant.toLocalDateTime(): LocalDateTime =
    ZonedDateTime.ofInstant(this, ZONE_ID_EUROPE_OSLO).toLocalDateTime()

fun Instant.toDate(): Date =
    Date.from(this)

operator fun Instant.minus(duration: Duration): Instant =
    minusNanos(duration.inWholeNanoseconds)

operator fun Instant.plus(duration: Duration): Instant =
    plusNanos(duration.inWholeNanoseconds)

fun Instant.leggTilArbeidsdager(days: Int): Instant =
    NorwegianDateUtil.addWorkingDaysToDate(toDate(), days).toInstant()

val Instant.erArbeidsdag: Boolean
    get() = NorwegianDateUtil.isWorkingDay(toDate())

val Instant.erIkkeArbeidsdag: Boolean
    get() = NorwegianDateUtil.isHoliday(toDate())

val Int.arbeidsdager: Instant get() = nå().leggTilArbeidsdager(this)

val TIME_ZONE_EUROPE_OSLO: TimeZone =
    TimeZone.getTimeZone("Europe/Oslo")

val ZONE_ID_EUROPE_OSLO: ZoneId =
    TIME_ZONE_EUROPE_OSLO.toZoneId()

val DATE_TIME_FORMATTER_NORWEGIAN_DATE: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd.MM.yyyy", LOCALE_NORWAY).withZone(ZONE_ID_EUROPE_OSLO)
