package no.nav.hjelpemidler.time

import java.time.Period
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount
import java.time.temporal.TemporalUnit
import java.time.temporal.UnsupportedTemporalTypeException

/**
 * @see [kotlin.Int.arbeidsdager]
 */
class Arbeidsdager private constructor(private val arbeidsdager: Int) : TemporalAmount, Comparable<Arbeidsdager> {
    private val zoneId = ZONE_ID_EUROPE_OSLO

    override fun get(unit: TemporalUnit?): Long {
        if (unit == ChronoUnit.DAYS) return arbeidsdager.toLong()
        throw UnsupportedTemporalTypeException("Unsupported unit: $unit")
    }

    override fun getUnits(): List<TemporalUnit> = listOf(ChronoUnit.DAYS)

    override fun addTo(temporal: Temporal): Temporal {
        if (arbeidsdager == 0) return temporal
        if (arbeidsdager < 0) return negate().subtractFrom(temporal)
        val start = temporal.asLocalDate(zoneId)
        var end = start
        repeat(arbeidsdager) {
            end = end.plusDays(1)
            while (end.isFridag) {
                end = end.plusDays(1)
            }
        }
        return temporal + daysBetween(start, end)
    }

    override fun subtractFrom(temporal: Temporal): Temporal {
        if (arbeidsdager == 0) return temporal
        if (arbeidsdager < 0) return negate().addTo(temporal)
        val end = temporal.asLocalDate(zoneId)
        var start = end
        repeat(arbeidsdager) {
            start = start.minusDays(1)
            while (start.isFridag) {
                start = start.minusDays(1)
            }
        }
        return temporal - daysBetween(start, end)
    }

    private fun negate() = Arbeidsdager(-arbeidsdager)

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

private fun daysBetween(temporal1Inclusive: Temporal, temporal2Exclusive: Temporal) =
    Period.ofDays(ChronoUnit.DAYS.between(temporal1Inclusive, temporal2Exclusive).toInt())
