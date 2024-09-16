package no.nav.hjelpemidler.domain.person

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import no.nav.hjelpemidler.test.jsonToValue
import no.nav.hjelpemidler.test.valueToJson
import kotlin.test.Test

class AktørIdTest {
    private val aktørId = AktørId("1234567891011")
    private val aktørIdJson = """"$aktørId""""

    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(aktørId) shouldBe aktørIdJson
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<AktørId>(aktørIdJson) shouldBe aktørId
    }

    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(aktørId) shouldBe aktørIdJson
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString<AktørId>(aktørIdJson) shouldBe aktørId
    }

    @Test
    fun `AktørId skal bestå av 13 siffer`() {
        shouldThrow<IllegalArgumentException> { "".toAktørId() }
        shouldThrow<IllegalArgumentException> { "abcdefghijklm".toAktørId() }
        shouldThrow<IllegalArgumentException> { "123456789101".toAktørId() }
        shouldThrow<IllegalArgumentException> { "12345678910111".toAktørId() }
        shouldNotThrowAny { "1234567891011".toAktørId() }
    }
}
