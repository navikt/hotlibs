package no.nav.hjelpemidler.domain.enhet

import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import kotlin.test.Test

class EnhetsnummerSerializerTest {
    private val enhetsnummer = Enhet.IT_AVDELINGEN.nummer
    private val enhetsnummerJson = """"$enhetsnummer""""

    @Test
    fun `Serialiser til JSON med kotlinx-serialization-json`() {
        Json.encodeToString(EnhetsnummerSerializer, enhetsnummer) shouldBe enhetsnummerJson
    }

    @Test
    fun `Deserialiser til Kotlin med kotlinx-serialization-json`() {
        Json.decodeFromString(EnhetsnummerSerializer, enhetsnummerJson) shouldBe enhetsnummer
    }
}
