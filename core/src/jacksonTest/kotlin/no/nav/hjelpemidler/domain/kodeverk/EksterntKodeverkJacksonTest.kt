package no.nav.hjelpemidler.domain.kodeverk

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import org.junit.jupiter.api.Test

class EksterntKodeverkJacksonTest {
    @Test
    fun `JSON skal inneholde ekstern kode`() {
        val json = valueToJson(Request(EksterntKodeverk.of("abc123")))
        json.shouldEqualJson("""{ "kode": "abc123" }""")
    }

    @Test
    fun `JSON skal inneholde null`() {
        val json = valueToJson(Request(EmptyEksterntKodeverk))
        json.shouldEqualJson("""{ "kode": null }""")
    }

    @Test
    fun `JSON to EksterntKodeverk`() {
        val request = jsonToValue<Request>("""{ "kode": "abc123" }""")
        request.kode?.eksternKode shouldBe "abc123"
    }

    @Test
    fun `JSON to null`() {
        val request1 = jsonToValue<Request>("""{ "kode": "" }""")
        request1.kode shouldBe null

        val request2 = jsonToValue<Request>("""{ "kode": null }""")
        request2.kode shouldBe null
    }
}

private object EmptyEksterntKodeverk : EksterntKodeverk {
    override val eksternKode: String = ""
}

private data class Request(
    @JsonSerializeEksterntKodeverk
    val kode: EksterntKodeverk?,
)
