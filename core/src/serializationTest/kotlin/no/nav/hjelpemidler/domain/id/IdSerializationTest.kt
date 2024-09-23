package no.nav.hjelpemidler.domain.id

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test

class IdSerializationTest {
    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        // Json.encodeToString(longId) shouldBe longIdJsonString
        Json.encodeToString(stringId) shouldBe stringIdJsonString
        Json.encodeToString(uuidId) shouldBe uuidIdJsonString
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString<TestLongId>(longIdJsonNumber) shouldBe longId
        Json.decodeFromString<TestLongId>(longIdJsonString) shouldBe longId
        Json.decodeFromString<TestStringId>(stringIdJsonString) shouldBe stringId
        Json.decodeFromString<TestUuidId>(uuidIdJsonString) shouldBe uuidId

        shouldThrow<SerializationException> { Json.decodeFromString<TestLongId>("""true""") }
        shouldThrow<SerializationException> { Json.decodeFromString<TestLongId>("""false""") }
        shouldThrow<SerializationException> { Json.decodeFromString<TestLongId>("""null""") }

        Json.decodeFromString<TestLongId?>("""null""") shouldBe null
        Json.decodeFromString<TestStringId?>("""null""") shouldBe null
        Json.decodeFromString<TestUuidId?>("""null""") shouldBe null
    }
}
