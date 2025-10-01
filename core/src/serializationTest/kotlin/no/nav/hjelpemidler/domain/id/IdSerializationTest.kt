package no.nav.hjelpemidler.domain.id

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.json.Json
import kotlin.test.Test

class IdSerializationTest {
    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(TestNumberIdSerializer, numberId) shouldBe numberIdJsonString
        Json.encodeToString(TestStringIdSerializer, stringId) shouldBe stringIdJsonString
        Json.encodeToString(TestUuidIdSerializer, uuidId) shouldBe uuidIdJsonString
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString(TestNumberIdSerializer, numberIdJsonNumber) shouldBe numberId
        Json.decodeFromString(TestNumberIdSerializer, numberIdJsonString) shouldBe numberId
        Json.decodeFromString(TestStringIdSerializer, stringIdJsonString) shouldBe stringId
        Json.decodeFromString(TestUuidIdSerializer, uuidIdJsonString) shouldBe uuidId

        shouldThrow<NumberFormatException> { Json.decodeFromString(TestNumberIdSerializer, """true""") }
        shouldThrow<NumberFormatException> { Json.decodeFromString(TestNumberIdSerializer, """false""") }
        shouldThrow<IllegalArgumentException> { Json.decodeFromString(TestNumberIdSerializer, """null""") }

        Json.decodeFromString(TestNumberIdSerializer.nullable, """null""") shouldBe null
        Json.decodeFromString(TestStringIdSerializer.nullable, """null""") shouldBe null
        Json.decodeFromString(TestUuidIdSerializer.nullable, """null""") shouldBe null
    }
}

private object TestNumberIdSerializer :
    IdSerializer<TestLongId>("no.nav.hjelpemidler.domain.id.TestNumberIdSerializer", ::TestLongId)

private object TestStringIdSerializer :
    IdSerializer<TestStringId>("no.nav.hjelpemidler.domain.id.TestStringIdSerializer", ::TestStringId)

private object TestUuidIdSerializer :
    IdSerializer<TestUuidId>("no.nav.hjelpemidler.domain.id.TestUuidIdSerializer", ::TestUuidId)
