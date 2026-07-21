package no.nav.hjelpemidler.domain.tilgang

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import kotlin.test.Test

class ApplikasjonsnavnJacksonTest {
    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(applikasjonsnavn) shouldBe applikasjonsnavnJson
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<Applikasjonsnavn>(applikasjonsnavnJson) shouldBe applikasjonsnavn
    }
}
