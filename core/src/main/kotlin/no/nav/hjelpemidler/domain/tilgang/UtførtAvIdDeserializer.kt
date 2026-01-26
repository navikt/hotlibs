package no.nav.hjelpemidler.domain.tilgang

import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer

internal class UtførtAvIdDeserializer : FromStringDeserializer<UtførtAvId>(UtførtAvId::class.java) {
    override fun _deserialize(value: String, context: DeserializationContext): UtførtAvId = utførtAvIdOf(value)
}
