package no.nav.hjelpemidler.domain.person

import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.deser.std.FromStringDeserializer

internal class PersonIdentDeserializer : FromStringDeserializer<PersonIdent>(PersonIdent::class.java) {
    override fun _deserialize(value: String, context: DeserializationContext): PersonIdent = personIdentOf(value)
}
