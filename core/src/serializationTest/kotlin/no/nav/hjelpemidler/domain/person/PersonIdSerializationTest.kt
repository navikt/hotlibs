package no.nav.hjelpemidler.domain.person

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlin.test.Test

class PersonIdSerializationTest {
    private val aktørId = lagTilfeldigAktørId()
    private val fnr = lagTilfeldigFødselsnummer()

    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(PersonIdSerializer, aktørId) shouldBe aktørId.toJson()
        Json.encodeToString(PersonIdSerializer, fnr) shouldBe fnr.toJson()
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString(PersonIdSerializer, aktørId.toJson()) shouldBe aktørId
        Json.decodeFromString(PersonIdSerializer, fnr.toJson()) shouldBe fnr
    }
}
