package no.nav.hjelpemidler.domain.id

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.json.Json
import kotlin.test.Test

class IdSerializationTest {
    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(TestLongIdSerializer, longId) shouldBe longIdJsonString
        Json.encodeToString(TestStringIdSerializer, stringId) shouldBe stringIdJsonString
        Json.encodeToString(TestUuidIdSerializer, uuidId) shouldBe uuidIdJsonString
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString(TestLongIdSerializer, longIdJsonNumber) shouldBe longId
        Json.decodeFromString(TestLongIdSerializer, longIdJsonString) shouldBe longId
        Json.decodeFromString(TestStringIdSerializer, stringIdJsonString) shouldBe stringId
        Json.decodeFromString(TestUuidIdSerializer, uuidIdJsonString) shouldBe uuidId

        shouldThrow<NumberFormatException> { Json.decodeFromString(TestLongIdSerializer, """true""") }
        shouldThrow<NumberFormatException> { Json.decodeFromString(TestLongIdSerializer, """false""") }
        shouldThrow<IllegalArgumentException> { Json.decodeFromString(TestLongIdSerializer, """null""") }

        Json.decodeFromString(TestLongIdSerializer.nullable, """null""") shouldBe null
        Json.decodeFromString(TestStringIdSerializer.nullable, """null""") shouldBe null
        Json.decodeFromString(TestUuidIdSerializer.nullable, """null""") shouldBe null
    }
}

private object TestLongIdSerializer :
    IdSerializer<TestLongId>("no.nav.hjelpemidler.domain.id.TestLongIdSerializer", { TestLongId(it.toLong()) })

private object TestStringIdSerializer :
    IdSerializer<TestStringId>("no.nav.hjelpemidler.domain.id.TestStringIdSerializer", ::TestStringId)

private object TestUuidIdSerializer :
    IdSerializer<TestUuidId>("no.nav.hjelpemidler.domain.id.TestUuidIdSerializer", { TestUuidId(UUID(it)) })
