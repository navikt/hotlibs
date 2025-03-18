package no.nav.hjelpemidler.domain.id

import com.fasterxml.jackson.annotation.JsonValue

/**
 * Abstrakt klasse for implementasjon av sterke typer for ulike identifikatorer.
 */
abstract class Id<T : Comparable<T>>(val value: T) : Comparable<Id<T>> {
    /**
     * NB! Denne implementasjonen vil gi `0` for lik [value], også når ulike implementasjoner av [Id]
     * sammenlignes. Dette skiller seg fra [equals] som også sammenligner [javaClass].
     */
    override fun compareTo(other: Id<T>): Int = value.compareTo(other.value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Id<*>
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    /**
     * Dette blir verdien i JSON.
     */
    @JsonValue
    override fun toString(): String = value.toString()
}
