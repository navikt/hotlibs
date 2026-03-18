package no.nav.hjelpemidler.domain.tilgang

import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.deser.std.FromStringDeserializer

internal class UtførtAvIdDeserializer : FromStringDeserializer<UtførtAvId>(UtførtAvId::class.java) {
    override fun _deserialize(value: String, context: DeserializationContext): UtførtAvId = utførtAvIdOf(value)
}
