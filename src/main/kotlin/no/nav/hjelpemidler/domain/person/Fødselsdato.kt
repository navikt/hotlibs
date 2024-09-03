package no.nav.hjelpemidler.domain.person

import no.nav.hjelpemidler.time.iDag
import java.time.LocalDate
import java.time.Period

typealias Fødselsdato = LocalDate

fun Fødselsdato.alder(dag: LocalDate = iDag()): Int = Period.between(this, dag).years

private infix fun Int.år(dag: LocalDate): Fødselsdato = dag.minusYears(toLong())

infix fun Int.`år på`(dag: LocalDate): Fødselsdato = this år dag

val Int.år: Fødselsdato get() = this år iDag()
