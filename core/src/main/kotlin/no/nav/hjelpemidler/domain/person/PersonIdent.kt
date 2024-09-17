package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonValue

sealed class PersonIdent(val value: String) : CharSequence by value {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PersonIdent
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    @JsonValue
    override fun toString(): String = value
}
