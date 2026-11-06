package no.nav.hjelpemidler.domain.enhet

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.Json
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import org.junit.jupiter.api.Test

class EnhetJacksonTest {
    private val json = Json(
        """
            {
              "nummer" : "2970",
              "navn" : "IT-avdelingen"
            }
        """.trimIndent()
    )

    @Test
    fun `Enhet til JSON`() {
        valueToJson(Enhet.IT_AVDELINGEN) shouldEqualJson json.toString()
    }

    @Test
    fun `JSON til Enhet`() {
        json.toValue<Enhet>() shouldBe Enhet.IT_AVDELINGEN
    }

    @Test
    fun `Kun nummer i JSON`() {
        Json("""{ "nr": "2990" }""").toValue<Enhet>() shouldBe Enhet(nummer = "2990", navn = "2990")
    }
}
