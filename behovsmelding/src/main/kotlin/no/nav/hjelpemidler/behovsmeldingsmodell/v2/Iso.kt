package no.nav.hjelpemidler.behovsmeldingsmodell.v2

import com.fasterxml.jackson.annotation.JsonValue
import no.nav.hjelpemidler.text.isInteger

data class Iso6(private val value: String) {
    init {
        require(value.length == 6 && value.isInteger()) { "Iso6 må være 6 siffer, '$value'" }
    }

    @JsonValue
    override fun toString(): String = value
}

data class Iso8(private val value: String) {
    init {
        require(value.length == 8 && value.isInteger()) { "Iso8 må være 8 siffer, '$value'" }
    }

    fun toIso6() = Iso6(value.take(6))

    @JsonValue
    override fun toString(): String = value
}
