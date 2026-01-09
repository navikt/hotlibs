package no.nav.hjelpemidler.domain.person

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlin.test.Test

class FødselsnummerSerializationTest {
    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(FødselsnummerSerializer, fnr) shouldBe fnrJson
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString(FødselsnummerSerializer, fnrJson) shouldBe fnr
    }
}
