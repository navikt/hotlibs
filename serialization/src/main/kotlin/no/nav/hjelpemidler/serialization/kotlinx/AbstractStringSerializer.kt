package no.nav.hjelpemidler.serialization.kotlinx

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

abstract class AbstractStringSerializer<T>(serialName: String) : KSerializer<T> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(serialName, PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: T) = encoder.encodeString(serialize(value))
    override fun deserialize(decoder: Decoder): T = deserialize(decoder.decodeString())

    abstract fun serialize(value: T): String
    abstract fun deserialize(value: String): T
}
