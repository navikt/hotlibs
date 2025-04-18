package no.nav.hjelpemidler.domain.kodeverk

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = KodeverkDeserializer::class)
interface Kodeverk<E : Enum<E>> {
    val name: String
}

class UkjentKode<E : Enum<E>>(override val name: String) : Kodeverk<E> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UkjentKode<*>
        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()

    @JsonValue
    override fun toString(): String = name
}
