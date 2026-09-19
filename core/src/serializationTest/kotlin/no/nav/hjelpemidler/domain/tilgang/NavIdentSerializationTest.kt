package no.nav.hjelpemidler.domain.tilgang

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlin.test.Test

class NavIdentSerializationTest {
    private val navIdent = lagTilfeldigNavIdent()

    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(NavIdentSerializer, navIdent) shouldBe navIdent.toJson()
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString(NavIdentSerializer, navIdent.toJson()) shouldBe navIdent
    }
}
