package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.annotation.JsonCreator
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import no.nav.hjelpemidler.logging.secureLog

@Serializable(with = AktørId.Serializer::class)
data class AktørId @JsonCreator constructor(override val value: String) : PersonIdent, CharSequence by value {
    init {
        val valid = value matches regex
        if (!valid) {
            secureLog.error { "Ugyldig aktørId: '$value'" }
            throw IllegalArgumentException("Ugyldig aktørId")
        }
    }

    override fun toString(): String = value

    internal class Serializer : KSerializer<AktørId> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
            "no.nav.hjelpemidler.domain.person.AktørId",
            PrimitiveKind.STRING,
        )

        override fun serialize(encoder: Encoder, value: AktørId) = encoder.encodeString(value.toString())
        override fun deserialize(decoder: Decoder): AktørId = AktørId(decoder.decodeString())
    }
}

private val regex: Regex = Regex("^[0-9]{13}$")

fun String.toAktørId(): AktørId = AktørId(this)
