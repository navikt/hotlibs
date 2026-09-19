package no.nav.hjelpemidler.domain.tilgang

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlin.test.Test

class ApplikasjonsnavnSerializationTest {
    private val applikasjonsnavn = Applikasjonsnavn("testApplication")

    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(ApplikasjonsnavnSerializer, applikasjonsnavn) shouldBe applikasjonsnavn.toJson()
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString(ApplikasjonsnavnSerializer, applikasjonsnavn.toJson()) shouldBe applikasjonsnavn
    }
}
