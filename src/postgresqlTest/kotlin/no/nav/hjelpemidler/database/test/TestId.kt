package no.nav.hjelpemidler.database.test

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import no.nav.hjelpemidler.database.QueryParameter

class TestId @JsonCreator constructor(@JsonValue override val queryParameter: Long = 0) : QueryParameter<Long> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TestId
        return queryParameter == other.queryParameter
    }

    override fun hashCode(): Int = queryParameter.hashCode()

    override fun toString(): String = queryParameter.toString()
}
