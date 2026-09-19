package no.nav.hjelpemidler.domain.id

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.json.Json
import java.util.UUID
import kotlin.test.Test

class IdSerializationTest {
    private val numberId = TestLongId(1000)
    private val stringId = TestStringId("2000")
    private val uuidId = TestUuidId(UUID.randomUUID())

    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(TestNumberIdSerializer, numberId) shouldBe numberId.toJson()
        Json.encodeToString(TestStringIdSerializer, stringId) shouldBe stringId.toJson()
        Json.encodeToString(TestUuidIdSerializer, uuidId) shouldBe uuidId.toJson()
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString(TestNumberIdSerializer, numberId.toJson()) shouldBe numberId
        Json.decodeFromString(TestNumberIdSerializer, numberId.toJson()) shouldBe numberId
        Json.decodeFromString(TestStringIdSerializer, stringId.toJson()) shouldBe stringId
        Json.decodeFromString(TestUuidIdSerializer, uuidId.toJson()) shouldBe uuidId

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
