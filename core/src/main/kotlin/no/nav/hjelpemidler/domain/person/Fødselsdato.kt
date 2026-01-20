package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.time.iDag
import java.time.LocalDate
import java.time.temporal.ChronoUnit

typealias Fødselsdato = LocalDate

fun Fødselsdato.alderPer(dag: LocalDate = iDag()): Int = ChronoUnit.YEARS.between(this, dag).toInt()

val Fødselsdato.alderIDag: Int get() = alderPer(iDag())

infix fun Int.år(dag: LocalDate): Fødselsdato = dag.minusYears(toLong())

val Int.år: Fødselsdato get() = this år iDag()
