package no.nav.hjelpemidler.time

import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount
import java.time.temporal.TemporalUnit
import java.time.temporal.UnsupportedTemporalTypeException
import kotlin.math.absoluteValue

/**
 * @see [kotlin.Int.arbeidsdager]
 */
class Arbeidsdager private constructor(private val arbeidsdager: Int) : TemporalAmount, Comparable<Arbeidsdager> {
    override fun get(unit: TemporalUnit?): Long {
        if (unit == ChronoUnit.DAYS) return arbeidsdager.toLong()
        throw UnsupportedTemporalTypeException("Unsupported unit: $unit")
    }

    override fun getUnits(): List<TemporalUnit> = listOf(ChronoUnit.DAYS)

    override fun addTo(temporal: Temporal): Temporal {
        if (arbeidsdager == 0) return temporal
        if (arbeidsdager < 0) return negated().subtractFrom(temporal)
        val days = countCalendarDays(LocalDate.from(temporal), step = 1)
        return temporal + Period.ofDays(days)
    }

    override fun subtractFrom(temporal: Temporal): Temporal {
        if (arbeidsdager == 0) return temporal
        if (arbeidsdager < 0) return negated().addTo(temporal)
        val days = countCalendarDays(LocalDate.from(temporal), step = -1)
        return temporal - Period.ofDays(days)
    }

    private fun negated(): Arbeidsdager = Arbeidsdager(-arbeidsdager)

    private fun countCalendarDays(from: LocalDate, step: Long): Int {
        var current = from
        repeat(arbeidsdager) {
            current = current.plusDays(step)
            while (current.isFridag) {
                current = current.plusDays(step)
            }
        }
        return ChronoUnit.DAYS.between(from, current).toInt().absoluteValue
    }

    override fun compareTo(other: Arbeidsdager): Int = this.arbeidsdager.compareTo(other.arbeidsdager)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Arbeidsdager
        return arbeidsdager == other.arbeidsdager
    }

    override fun hashCode(): Int = arbeidsdager

    override fun toString(): String = arbeidsdager.toString()

    companion object {
        val ZERO = Arbeidsdager(0)
        val ONE = Arbeidsdager(1)
        val THIRTY = Arbeidsdager(30)

        internal fun of(arbeidsdager: Int) = when (arbeidsdager) {
            0 -> ZERO
            1 -> ONE
            30 -> THIRTY
            else -> Arbeidsdager(arbeidsdager)
        }
    }
}
