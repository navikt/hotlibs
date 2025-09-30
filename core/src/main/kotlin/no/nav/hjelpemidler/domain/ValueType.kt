package no.nav.hjelpemidler.domain

import java.io.Serializable

/**
 * Wrapper for en enkelt verdi.
 */
interface ValueType<T : Comparable<T>> : Serializable {
    val value: T
}
