package no.nav.hjelpemidler.domain.person

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlin.test.Test

class PersonIdentSerializationTest {
    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(PersonIdentSerializer, aktørId) shouldBe aktørIdJson
        Json.encodeToString(PersonIdentSerializer, fnr) shouldBe fnrJson
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString(PersonIdentSerializer, aktørIdJson) shouldBe aktørId
        Json.decodeFromString(PersonIdentSerializer, fnrJson) shouldBe fnr
    }
}
