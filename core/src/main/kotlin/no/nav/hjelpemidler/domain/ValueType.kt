package no.nav.hjelpemidler.domain

/**
 * Wrapper for en enkelt verdi.
 */
interface ValueType<T : Any> {
    val value: T
}
