package no.nav.hjelpemidler.domain.tilgang

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlin.test.Test

class UtførtAvIdSerializationTest {
    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(UtførtAvIdSerializer, navIdent) shouldBe navIdentJson
        Json.encodeToString(UtførtAvIdSerializer, systemnavn) shouldBe systemnavnJson
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString(UtførtAvIdSerializer, navIdentJson) shouldBe navIdent
        Json.decodeFromString(UtførtAvIdSerializer, systemnavnJson) shouldBe systemnavn
    }
}
