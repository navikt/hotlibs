package no.nav.hjelpemidler.validation

interface Validator<T> {
    fun erGyldig(value: T): Boolean
}
