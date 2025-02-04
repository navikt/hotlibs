package no.nav.hjelpemidler.nare.core

fun <T : LogiskOperand<T>> ikke(operand: LogiskOperand<T>): T = operand.ikke()
