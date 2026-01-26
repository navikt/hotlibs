package no.nav.hjelpemidler.domain.tilgang

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import kotlin.test.Test

class NavIdentJacksonTest {
    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(navIdent) shouldBe navIdentJson
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<NavIdent>(navIdentJson) shouldBe navIdent
    }
}
