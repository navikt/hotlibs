package no.nav.hjelpemidler.domain.person

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import no.nav.hjelpemidler.serialization.serialName

internal abstract class PersonIdentSerializer<T : PersonIdent> : KSerializer<T> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(this::class.serialName, PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: T) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): T = deserialize(decoder.decodeString())
    abstract fun deserialize(value: String): T
}
