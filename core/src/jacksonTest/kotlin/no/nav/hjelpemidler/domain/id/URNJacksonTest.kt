package no.nav.hjelpemidler.domain.id

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.test.jsonToValue
import no.nav.hjelpemidler.test.valueToJson
import kotlin.test.Test

class URNJacksonTest {
    private val urn = URN("hotlibs", "test", "1")

    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(urn) shouldBe "\"urn:hotlibs:test:1\""
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<URN>("\"urn:hotlibs:test:1\"") shouldBe urn
    }
}
