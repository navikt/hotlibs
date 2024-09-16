package no.nav.hjelpemidler.domain.id

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import no.nav.hjelpemidler.test.jsonToValue
import no.nav.hjelpemidler.test.valueToJson
import java.util.UUID
import kotlin.test.Test

class IdTest {
    private val longId = TestLongId(12345)
    private val stringId = TestStringId("54321")
    private val uuidId = TestUuidId(UUID())

    private val longIdJsonNumber = "$longId"
    private val longIdJsonString = """"$longId""""
    private val stringIdJsonString = """"$stringId""""
    private val uuidIdJsonString = """"$uuidId""""

    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(longId) shouldBe longIdJsonString
        valueToJson(stringId) shouldBe stringIdJsonString
        valueToJson(uuidId) shouldBe uuidIdJsonString
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<TestLongId>(longIdJsonNumber) shouldBe longId
        jsonToValue<TestLongId>(longIdJsonString) shouldBe longId
        jsonToValue<TestStringId>(stringIdJsonString) shouldBe stringId
        jsonToValue<TestUuidId>(uuidIdJsonString) shouldBe uuidId
    }

    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(longId) shouldBe longIdJsonString
        Json.encodeToString(stringId) shouldBe stringIdJsonString
        Json.encodeToString(uuidId) shouldBe uuidIdJsonString
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString<TestLongId>(longIdJsonNumber) shouldBe longId
        Json.decodeFromString<TestLongId>(longIdJsonString) shouldBe longId
        Json.decodeFromString<TestStringId>(stringIdJsonString) shouldBe stringId
        Json.decodeFromString<TestUuidId>(uuidIdJsonString) shouldBe uuidId

        shouldThrow<NumberFormatException> { Json.decodeFromString<TestLongId>("""true""") }
        shouldThrow<NumberFormatException> { Json.decodeFromString<TestLongId>("""false""") }
        shouldThrow<IllegalStateException> { Json.decodeFromString<TestLongId>("""null""") }

        Json.decodeFromString<TestLongId?>("""null""") shouldBe null
        Json.decodeFromString<TestStringId?>("""null""") shouldBe null
        Json.decodeFromString<TestUuidId?>("""null""") shouldBe null
    }
}

@Serializable(with = TestLongId.Serializer::class)
class TestLongId(value: Long) : Id<Long>(value) {
    object Serializer : Id.Serializer<TestLongId>() {
        override fun deserialize(value: String): TestLongId = TestLongId(value.toLong())
    }
}

@Serializable(with = TestStringId.Serializer::class)
class TestStringId(value: String) : Id<String>(value) {
    object Serializer : Id.Serializer<TestStringId>() {
        override fun deserialize(value: String): TestStringId = TestStringId(value)
    }
}

@Serializable(with = TestUuidId.Serializer::class)
class TestUuidId(value: UUID) : Id<UUID>(value) {
    object Serializer : Id.Serializer<TestUuidId>() {
        override fun deserialize(value: String): TestUuidId = TestUuidId(value.toUUID())
    }
}
