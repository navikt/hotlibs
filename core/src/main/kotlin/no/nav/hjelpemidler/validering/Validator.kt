package no.nav.hjelpemidler.validering

fun interface Validator<in T> {
    operator fun invoke(value: T): Boolean
}
