package no.nav.hjelpemidler.domain.tilgang

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import kotlin.test.Test

class SystemnavnJacksonTest {
    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(systemnavn) shouldBe systemnavnJson
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<Systemnavn>(systemnavnJson) shouldBe systemnavn
    }
}
