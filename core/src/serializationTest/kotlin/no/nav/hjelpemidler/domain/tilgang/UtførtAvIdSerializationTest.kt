package no.nav.hjelpemidler.domain.tilgang

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlin.test.Test

class UtførtAvIdSerializationTest {
    private val navIdent = lagTilfeldigNavIdent()
    private val applikasjonsnavn = Applikasjonsnavn("testApplication")

    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(UtførtAvIdSerializer, navIdent) shouldBe navIdent.toJson()
        Json.encodeToString(UtførtAvIdSerializer, applikasjonsnavn) shouldBe applikasjonsnavn.toJson()
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString(UtførtAvIdSerializer, navIdent.toJson()) shouldBe navIdent
        Json.decodeFromString(UtførtAvIdSerializer, applikasjonsnavn.toJson()) shouldBe applikasjonsnavn
    }
}
