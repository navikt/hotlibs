package no.nav.hjelpemidler.domain.id

import com.fasterxml.jackson.annotation.JsonValue
import java.nio.charset.Charset

/**
 * Abstrakt klasse for implementasjon av sterke typer for ulike identifikatorer.
 */
abstract class Id<T : Comparable<T>>(val value: T) : Comparable<Id<T>> {
    fun toByteArray(charset: Charset = Charsets.UTF_8): ByteArray = toString().toByteArray(charset)

    override fun compareTo(other: Id<T>): Int {
        val valueComparison = value.compareTo(other.value)
        return if (valueComparison == 0) javaClass.name.compareTo(other.javaClass.name) else valueComparison
    }

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
