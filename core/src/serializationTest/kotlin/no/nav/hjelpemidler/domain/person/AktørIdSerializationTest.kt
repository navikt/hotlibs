package no.nav.hjelpemidler.domain.person

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlin.test.Test

class AktørIdSerializationTest {
    private val aktørId = lagTilfeldigAktørId()
    private val aktørIdJson = aktørId.toJson()

    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(AktørIdSerializer, aktørId) shouldBe aktørIdJson
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString(AktørIdSerializer, aktørIdJson) shouldBe aktørId
    }
}
