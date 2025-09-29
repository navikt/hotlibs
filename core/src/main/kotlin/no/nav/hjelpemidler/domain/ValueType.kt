package no.nav.hjelpemidler.domain

/**
 * Wrapper for en enkelt verdi.
 */
interface ValueType<T : Comparable<T>> {
    val value: T
}
