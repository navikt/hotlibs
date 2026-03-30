package no.nav.hjelpemidler.time

import java.time.DayOfWeek
import java.time.LocalDate

infix fun LocalDate.erISammeÅrSom(other: LocalDate): Boolean = year == other.year

/**
 * @see [java.time.LocalDate.isFridag]
 */
val LocalDate.isArbeidsdag: Boolean get() = !isFridag

/**
 * @see [java.time.LocalDate.isArbeidsdag]
 */
val LocalDate.isFridag: Boolean
    get() = dayOfWeek.let { it == DayOfWeek.SATURDAY || it == DayOfWeek.SUNDAY } || this in fridagerI(year)
