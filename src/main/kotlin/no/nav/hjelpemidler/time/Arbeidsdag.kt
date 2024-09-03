package no.nav.hjelpemidler.time

import no.bekk.bekkopen.date.NorwegianDateUtil
import java.time.Instant

/**
 * Tidspunkt etter X arbeidsdager.
 *
 * @see [Instant.leggTilArbeidsdager]
 */
val Int.arbeidsdager: Instant get() = nå().leggTilArbeidsdager(this)

/**
 * @see [NorwegianDateUtil.addWorkingDaysToDate]
 */
infix fun Instant.leggTilArbeidsdager(days: Int): Instant =
    NorwegianDateUtil.addWorkingDaysToDate(toDate(), days).toInstant()

/**
 * @see [NorwegianDateUtil.isWorkingDay]
 */
val Instant.erArbeidsdag: Boolean
    get() = NorwegianDateUtil.isWorkingDay(toDate())

/**
 * @see [NorwegianDateUtil.isHoliday]
 */
val Instant.erFeriedag: Boolean
    get() = NorwegianDateUtil.isHoliday(toDate())
