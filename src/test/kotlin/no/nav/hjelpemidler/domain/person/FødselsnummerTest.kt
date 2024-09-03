package no.nav.hjelpemidler.domain.person

import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import no.nav.hjelpemidler.test.jsonToValue
import no.nav.hjelpemidler.test.valueToJson
import kotlin.test.Test

class FødselsnummerTest {
    private val fnr = Fødselsnummer(30.år)
    private val fnrJson = """"$fnr""""

    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(fnr) shouldBe fnrJson
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<Fødselsnummer>(fnrJson) shouldBe fnr
    }

    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(fnr) shouldBe fnrJson
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString<Fødselsnummer>(fnrJson) shouldBe fnr
    }
}
