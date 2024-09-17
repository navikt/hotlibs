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
import no.nav.hjelpemidler.serialization.serialName

abstract class IdSerializer<T : Id<*>> : KSerializer<T> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(this::class.serialName, PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: T) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): T =
        when (decoder) {
            is JsonDecoder -> deserialize(checkNotNull(decoder.decodeJsonElement().jsonPrimitive.contentOrNull))
            else -> deserialize(decoder.decodeString())
        }

    abstract fun deserialize(value: String): T
}
