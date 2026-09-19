package no.nav.hjelpemidler.domain.person

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import kotlin.test.Test

class AktørIdJacksonTest {
    private val aktørId = lagTilfeldigAktørId()

    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(aktørId) shouldBe aktørId.toJson()
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<AktørId>(aktørId.toJson()) shouldBe aktørId
    }
}
