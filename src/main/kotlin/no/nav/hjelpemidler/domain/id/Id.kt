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
import no.nav.hjelpemidler.serialization.serialName

/**
 * Klasse som danner grunnlag for implementasjon av sterke typer for ulike identifikatorer.
 * Dette som et alternativ til å bruke primitive typer direkte.
 *
 * Bruk av [JsonValue] sikrer at kun [value] havner i JSON ved serialisering.
 * Konstruktøren(e) i konkrete implementasjoner brukes ved deserialisering.
 * Bruk evt. [com.fasterxml.jackson.annotation.JsonCreator] for å markere hvilke(n) konstruktør(er) som
 * skal benyttes.
 *
 * Det er også lagt opp til bruk av ktor-resources med [Id.Serializer]. Implementasjoner av denne sikrer
 * riktig (de)serialisering ved bruk av kotlinx-serialization som brukes under panseret i ktor-resources.
 *
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

    abstract class Serializer<T : Id<*>> : KSerializer<T> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor(this::class.serialName, PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: T) =
            encoder.encodeString(value.toString())

        override fun deserialize(decoder: Decoder): T =
            when (decoder) {
                is JsonDecoder -> deserialize(checkNotNull(decoder.decodeJsonElement().jsonPrimitive.contentOrNull))
                else -> deserialize(decoder.decodeString())
            }

        abstract fun deserialize(value: String): T
    }
}
