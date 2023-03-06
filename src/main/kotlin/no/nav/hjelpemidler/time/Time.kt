package no.nav.hjelpemidler.time

import java.time.Instant
import kotlin.time.Duration
import kotlin.time.toJavaDuration

fun now(): Instant =
    Instant.now()

operator fun Instant.plus(amountToAdd: Duration): Instant =
    this.plus(amountToAdd.toJavaDuration())

operator fun Instant.minus(amountToSubtract: Duration): Instant =
    this.minus(amountToSubtract.toJavaDuration())
