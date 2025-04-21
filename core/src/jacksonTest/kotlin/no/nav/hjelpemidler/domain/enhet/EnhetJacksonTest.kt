package no.nav.hjelpemidler.domain.enhet

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.domain.geografi.Enhet
import no.nav.hjelpemidler.serialization.jackson.Json
import no.nav.hjelpemidler.serialization.jackson.toJson
import kotlin.test.Test

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
        Enhet.IT_AVDELINGEN.toJson() shouldEqualJson json.toString()
    }

    @Test
    fun `JSON til Enhet`() {
        json.toValue<Enhet>() shouldBe Enhet.IT_AVDELINGEN
    }
}
