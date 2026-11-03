package no.nav.hjelpemidler.pagination

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import org.junit.jupiter.api.Test

class PageRequestJacksonTest {
    private val request = PageRequest(pageNumber = 1, pageSize = 10)
    private val requestJson = """{ "pageNumber": 1, "pageSize": 10 }"""

    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(request) shouldEqualJson requestJson
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<PageRequest>(requestJson) shouldBe request
    }
}
