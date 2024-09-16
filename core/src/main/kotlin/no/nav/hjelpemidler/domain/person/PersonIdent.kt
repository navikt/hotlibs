package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonValue
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import no.nav.hjelpemidler.serialization.serialName

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

    internal abstract class Serializer<T : PersonIdent> : KSerializer<T> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor(this::class.serialName, PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: T) = encoder.encodeString(value.toString())
        override fun deserialize(decoder: Decoder): T = deserialize(decoder.decodeString())
        abstract fun deserialize(value: String): T
    }
}
