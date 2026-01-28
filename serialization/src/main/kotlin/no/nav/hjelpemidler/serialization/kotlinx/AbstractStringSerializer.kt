package no.nav.hjelpemidler.serialization.kotlinx

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

abstract class AbstractStringSerializer<T>(
    serialName: String,
    private val toString: (value: T) -> String,
    private val fromString: (value: String) -> T,
) : KSerializer<T> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(serialName, PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: T) = encoder.encodeString(toString(value))
    override fun deserialize(decoder: Decoder): T = fromString(decoder.decodeString())
}
