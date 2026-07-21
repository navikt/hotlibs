package no.nav.hjelpemidler.domain.tilgang

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlin.test.Test

class ApplikasjonsnavnSerializationTest {
    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(ApplikasjonsnavnSerializer, applikasjonsnavn) shouldBe applikasjonsnavnJson
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString(ApplikasjonsnavnSerializer, applikasjonsnavnJson) shouldBe applikasjonsnavn
    }
}
