package no.nav.hjelpemidler.domain.person

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlin.test.Test

class PersonIdSerializationTest {
    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(PersonIdSerializer, aktørId) shouldBe aktørIdJson
        Json.encodeToString(PersonIdSerializer, fnr) shouldBe fnrJson
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString(PersonIdSerializer, aktørIdJson) shouldBe aktørId
        Json.decodeFromString(PersonIdSerializer, fnrJson) shouldBe fnr
    }
}
