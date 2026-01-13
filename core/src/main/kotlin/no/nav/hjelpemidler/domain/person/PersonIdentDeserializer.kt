package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer

internal class PersonIdentDeserializer : FromStringDeserializer<PersonIdent>(PersonIdent::class.java) {
    override fun _deserialize(value: String, context: DeserializationContext): PersonIdent =
        value.toPersonIdentOrThrow()
}
