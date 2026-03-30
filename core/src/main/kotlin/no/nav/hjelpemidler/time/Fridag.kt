package no.nav.hjelpemidler.time

import java.time.LocalDate
import java.time.Month
import java.time.MonthDay
import java.util.concurrent.ConcurrentHashMap

/**
 * Inkluderer alle faste, norske helligdager og høytidsdager.
 *
 * @see <a href="https://no.wikipedia.org/wiki/Offentlig_fridag">Offentlig fridag</a>
 */
private val fasteFridager: Set<MonthDay> = setOf(
    // 1. nyttårsdag
    MonthDay.of(Month.JANUARY, 1),
    // 1. mai
    MonthDay.of(Month.MAY, 1),
    // 17. mai
    MonthDay.of(Month.MAY, 17),
    // 1. juledag
    MonthDay.of(Month.DECEMBER, 25),
    // 2. juledag
    MonthDay.of(Month.DECEMBER, 26),
)

private val fridagerByÅr: MutableMap<Int, Set<LocalDate>> = ConcurrentHashMap()

internal fun fridagerI(år: Int): Set<LocalDate> = fridagerByÅr.computeIfAbsent(år) { år ->
    val fridager = sortedSetOf<LocalDate>()

    fasteFridager.mapTo(fridager) { it.atYear(år) }

    // bevegelige helligdager
    val førstePåskedag = utledFørstePåskedagI(år)
    // Skjærtorsdag
    fridager += førstePåskedag - 3.dager
    // Langfredag
    fridager += førstePåskedag - 2.dager
    // 1. påskedag (også søndag)
    fridager += førstePåskedag
    // 2. påskedag
    fridager += førstePåskedag + 1.dager
    // Kristi himmelfartsdag
    fridager += førstePåskedag + 39.dager
    // 1. pinsedag (også søndag)
    fridager += førstePåskedag + 49.dager
    // 2. pinsedag
    fridager += førstePåskedag + 50.dager

    fridager
}

/**
 * Kalles også "Meeus/Jones/Butcher"-algoritmen.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Date_of_Easter#Anonymous_Gregorian_algorithm">Anonymous Gregorian algorithm</a>
 */
internal fun utledFørstePåskedagI(år: Int): LocalDate {
    require(år >= 1583) { "Kan ikke utlede første påskedag før 1583" }

    val a = år % 19
    val b = år / 100
    val c = år % 100
    val d = b / 4
    val e = b % 4
    val f = (b + 8) / 25
    val g = (b - f + 1) / 3
    val h = (19 * a + b - d - g + 15) % 30
    val i = c / 4
    val k = c % 4
    val l = (32 + 2 * e + 2 * i - h - k) % 7
    val m = (a + 11 * h + 22 * l) / 451
    val month = (h + l - 7 * m + 114) / 31
    val dayOfMonth = ((h + l - 7 * m + 114) % 31) + 1

    return LocalDate.of(år, month, dayOfMonth)
}
