package no.nav.hjelpemidler.domain.person

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import kotlin.test.Test

class AktørIdJacksonTest {
    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(aktørId) shouldBe aktørIdJson
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<AktørId>(aktørIdJson) shouldBe aktørId
    }
}
