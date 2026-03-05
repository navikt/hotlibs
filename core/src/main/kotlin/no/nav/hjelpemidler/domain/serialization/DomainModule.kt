package no.nav.hjelpemidler.domain.serialization

import no.nav.hjelpemidler.domain.kodeverk.Kodeverk
import no.nav.hjelpemidler.domain.kodeverk.KodeverkDeserializer
import tools.jackson.databind.module.SimpleModule

class DomainModule : SimpleModule() {
    init {
        // Kodeverk
        addDeserializer(Kodeverk::class.java, KodeverkDeserializer())

        // JsonNullable
        addSerializer(JsonNullable::class.java, JsonNullableSerializer())
        addDeserializer(JsonNullable::class.java, JsonNullableDeserializer())
    }
}
