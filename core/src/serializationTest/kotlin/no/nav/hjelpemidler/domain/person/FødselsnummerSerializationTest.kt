package no.nav.hjelpemidler.domain.person

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlin.test.Test

class FødselsnummerSerializationTest {
    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(FødselsnummerSerializer, fnr) shouldBe fnrJson
        // Json.encodeToString(Person1(fnr)) shouldBe person1Json
        // Json.encodeToString(Person2(null)) shouldBe person2Json
        // Json.encodeToString(listOf(fnr)) shouldBe fnrAsArrayElementJson
        // Json.encodeToString(mapOf(fnr to true)) shouldBe fnrAsMapKeyJson
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString(FødselsnummerSerializer, fnrJson) shouldBe fnr
        // Json.decodeFromString<Person1>(person1Json) shouldBe Person1(fnr)
        // Json.decodeFromString<Person2>(person2Json) shouldBe Person2(null)
        // Json.decodeFromString<List<Fødselsnummer>>(fnrAsArrayElementJson) shouldBe listOf(fnr)
        // Json.decodeFromString<Map<Fødselsnummer, Boolean>>(fnrAsMapKeyJson) shouldBe mapOf(fnr to true)
    }
}
