package no.nav.hjelpemidler.nare.core

interface LogiskOperand<T : LogiskOperand<T>> {
    infix fun og(annen: T): T
    infix fun eller(annen: T): T
    fun ikke(): T
}
