package no.nav.hjelpemidler.domain.tilgang

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import kotlin.test.Test

class ApplikasjonsnavnJacksonTest {
    private val applikasjonsnavn = Applikasjonsnavn("testApplication")

    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(applikasjonsnavn) shouldBe applikasjonsnavn.toJson()
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<Applikasjonsnavn>(applikasjonsnavn.toJson()) shouldBe applikasjonsnavn
    }
}
