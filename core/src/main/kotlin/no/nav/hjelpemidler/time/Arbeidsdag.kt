package no.nav.hjelpemidler.time

import no.bekk.bekkopen.date.NorwegianDateUtil
import java.time.Instant
import java.time.ZonedDateTime

/**
 * Tidspunkt etter X arbeidsdager.
 *
 * @see [Instant.leggTilArbeidsdager]
 */
val Int.arbeidsdager: Instant get() = nå() leggTilArbeidsdager this

/**
 * @see [NorwegianDateUtil.addWorkingDaysToDate]
 */
infix fun Instant.leggTilArbeidsdager(arbeidsdager: Int): Instant =
    NorwegianDateUtil.addWorkingDaysToDate(toDate(), arbeidsdager).toInstant()

/**
 * @see [Instant.leggTilArbeidsdager]
 */
infix fun ZonedDateTime.leggTilArbeidsdager(arbeidsdager: Int): ZonedDateTime =
    toInstant().leggTilArbeidsdager(arbeidsdager).toZonedDateTime()

/**
 * @see [NorwegianDateUtil.isWorkingDay]
 */
val Instant.erArbeidsdag: Boolean
    get() = NorwegianDateUtil.isWorkingDay(toDate())

/**
 * @see [Instant.erArbeidsdag]
 */
val ZonedDateTime.erArbeidsdag: Boolean
    get() = toInstant().erArbeidsdag

/**
 * @see [NorwegianDateUtil.isHoliday]
 */
val Instant.erFeriedag: Boolean
    get() = NorwegianDateUtil.isHoliday(toDate())

/**
 * @see [Instant.erFeriedag]
 */
val ZonedDateTime.erFeriedag: Boolean
    get() = toInstant().erFeriedag
