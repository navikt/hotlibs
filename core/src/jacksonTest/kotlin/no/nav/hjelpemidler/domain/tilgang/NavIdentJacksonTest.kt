package no.nav.hjelpemidler.domain.tilgang

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import kotlin.test.Test

class NavIdentJacksonTest {
    private val navIdent = lagTilfeldigNavIdent()

    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(navIdent) shouldBe navIdent.toJson()
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<NavIdent>(navIdent.toJson()) shouldBe navIdent
    }
}
