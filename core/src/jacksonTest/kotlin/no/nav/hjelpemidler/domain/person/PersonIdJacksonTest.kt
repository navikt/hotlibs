package no.nav.hjelpemidler.domain.person

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import tools.jackson.databind.exc.ValueInstantiationException
import kotlin.test.Test

class PersonIdJacksonTest {
    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(aktørId) shouldBe aktørIdJson
        valueToJson(fnr) shouldBe fnrJson
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<PersonId?>("""null""") shouldBe null
        jsonToValue<PersonId>(aktørIdJson) shouldBe aktørId
        jsonToValue<PersonId>(fnrJson) shouldBe fnr
        shouldThrow<ValueInstantiationException> { jsonToValue<PersonId>(""""foobar"""") }

        jsonToValue<Request>("""{}""") shouldBe Request(null)
        jsonToValue<Request>("""{ "ident": null }""") shouldBe Request(null)
        jsonToValue<Request>("""{ "ident": "$aktørId" }""") shouldBe Request(aktørId)
        jsonToValue<Request>("""{ "ident": "$fnr" }""") shouldBe Request(fnr)
        shouldThrow<ValueInstantiationException> { jsonToValue<Request>("""{ "ident": "foobar" }""") }
    }
}

private data class Request(val ident: PersonId?)
