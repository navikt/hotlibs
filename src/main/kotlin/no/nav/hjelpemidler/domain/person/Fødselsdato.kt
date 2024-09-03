package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.time.iDag
import java.time.LocalDate
import java.time.Period

typealias Fødselsdato = LocalDate

fun Fødselsdato.alder(dag: LocalDate = iDag()): Int = Period.between(this, dag).years

infix fun Int.år(dag: LocalDate): Fødselsdato = dag.minusYears(toLong())

val Int.år: Fødselsdato get() = this år iDag()
