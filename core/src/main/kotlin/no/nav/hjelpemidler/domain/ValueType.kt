package no.nav.hjelpemidler.domain

import java.io.Serializable

/**
 * Wrapper for en enkelt verdi.
 */
interface ValueType<T : Any> : Serializable {
    val value: T
}
