package no.nav.hjelpemidler.nare.core

interface LogiskOperand<T : LogiskOperand<T>> {
    infix fun og(annen: T): T
    infix fun eller(annen: T): T
    fun ikke(): T
}

fun <T : LogiskOperand<T>> og(vararg operand: T): T = operand.reduce(LogiskOperand<T>::og)
fun <T : LogiskOperand<T>> eller(vararg operand: T): T = operand.reduce(LogiskOperand<T>::eller)
fun <T : LogiskOperand<T>> ikke(operand: LogiskOperand<T>): T = operand.ikke()
