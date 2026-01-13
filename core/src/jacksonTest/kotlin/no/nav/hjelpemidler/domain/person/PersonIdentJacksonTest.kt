package no.nav.hjelpemidler.domain.person

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import kotlin.test.Test

class PersonIdentJacksonTest {
    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(aktørId) shouldBe aktørIdJson
        valueToJson(fnr) shouldBe fnrJson
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<PersonIdent?>("""null""") shouldBe null
        jsonToValue<PersonIdent>(aktørIdJson) shouldBe aktørId
        jsonToValue<PersonIdent>(fnrJson) shouldBe fnr
        shouldThrow<InvalidFormatException> { jsonToValue<PersonIdent>(""""foobar"""") }

        jsonToValue<Request>("""{}""") shouldBe Request(null)
        jsonToValue<Request>("""{ "ident": null }""") shouldBe Request(null)
        jsonToValue<Request>("""{ "ident": "$aktørId" }""") shouldBe Request(aktørId)
        jsonToValue<Request>("""{ "ident": "$fnr" }""") shouldBe Request(fnr)
        shouldThrow<InvalidFormatException> { jsonToValue<Request>("""{ "ident": "foobar" }""") }
    }
}

private data class Request(val ident: PersonIdent?)
