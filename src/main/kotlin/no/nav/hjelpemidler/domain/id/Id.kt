package no.nav.hjelpemidler.domain.id

import com.fasterxml.jackson.annotation.JsonValue
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

/**
 * Abstrakt klasse for implementasjon av sterke typer for ulike identifikatorer.
 * Dette er et alternativ til å bruk primitive typer direkte.
 * Bruk av [JsonValue] sikrer at kun [value] havner i JSON ved serialisering.
 * NB! Vi serialiserer alltid verdien til [String] i JSON.
 */
abstract class Id<out T : Any>(val value: T) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Id<*>
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    @JsonValue
    override fun toString(): String = value.toString()

    abstract class Serializer<T : Id<*>>(serialName: String) : KSerializer<T> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(serialName, PrimitiveKind.STRING)
        override fun serialize(encoder: Encoder, value: T) = encoder.encodeString(value.toString())
        override fun deserialize(decoder: Decoder): T = when (decoder) {
            is JsonDecoder -> deserialize(
                decoder
                    .decodeJsonElement()
                    .jsonPrimitive.contentOrNull ?: error("Forventet en verdi, var null")
            )

            else -> deserialize(decoder.decodeString())
        }

        abstract fun deserialize(value: String): T
    }
}
