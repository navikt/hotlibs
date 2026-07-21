package no.nav.hjelpemidler.domain.tilgang

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import kotlin.test.Test

class UtførtAvIdJacksonTest {
    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(navIdent) shouldBe navIdentJson
        valueToJson(applikasjonsnavn) shouldBe applikasjonsnavnJson
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<UtførtAvId?>("""null""") shouldBe null
        jsonToValue<UtførtAvId>(navIdentJson) shouldBe navIdent
        jsonToValue<UtførtAvId>(applikasjonsnavnJson) shouldBe applikasjonsnavn

        jsonToValue<Request>("""{}""") shouldBe Request(null)
        jsonToValue<Request>("""{ "id": null }""") shouldBe Request(null)
        jsonToValue<Request>("""{ "id": "$navIdent" }""") shouldBe Request(navIdent)
        jsonToValue<Request>("""{ "id": "$applikasjonsnavn" }""") shouldBe Request(applikasjonsnavn)
    }
}

private data class Request(val id: UtførtAvId?)
