package no.nav.hjelpemidler.domain.id

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

abstract class IdSerializer<T : Id<*>>(
    serialName: String,
    private val creator: (rawValue: String) -> T,
) : KSerializer<T> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(serialName, PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: T) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): T {
        val rawValue = when (decoder) {
            is JsonDecoder -> decoder.decodeJsonElement().jsonPrimitive.contentOrNull ?: throw IllegalArgumentException(
                "Mangler verdi"
            )

            else -> decoder.decodeString()
        }
        return creator(rawValue)
    }
}
