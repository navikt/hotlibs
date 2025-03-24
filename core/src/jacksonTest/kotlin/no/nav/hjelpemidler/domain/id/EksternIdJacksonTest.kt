package no.nav.hjelpemidler.domain.id

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.test.jsonToValue
import no.nav.hjelpemidler.test.valueToJson
import kotlin.test.Test

class EksternIdJacksonTest {
    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(eksternId) shouldBe "\"$eksternId\""
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<EksternId>("\"$eksternId\"") shouldBe eksternId
    }
}
