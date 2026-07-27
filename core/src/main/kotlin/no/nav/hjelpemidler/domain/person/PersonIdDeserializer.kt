package no.nav.hjelpemidler.domain.person

import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.deser.std.FromStringDeserializer

internal class PersonIdDeserializer : FromStringDeserializer<PersonId>(PersonId::class.java) {
    override fun _deserialize(value: String, context: DeserializationContext): PersonId = personIdOf(value)
}
