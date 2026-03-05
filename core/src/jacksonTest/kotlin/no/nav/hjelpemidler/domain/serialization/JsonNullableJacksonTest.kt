package no.nav.hjelpemidler.domain.serialization

import com.fasterxml.jackson.annotation.JsonInclude
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.domain.enhet.Enhet
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import java.util.UUID
import kotlin.test.Test

class JsonNullableJacksonTest {
    private val id = UUID.randomUUID()
    private val fnr = Fødselsnummer(40.år)
    private val enhetsnummer = Enhet.IT_AVDELINGEN.nummer

    @Test
    fun `Verdier som mangler i JSON blir til Undefined`() {
        val json = "{}"
        val request = jsonToValue<TestRequest>(json)
        assertSoftly(request) {
            id shouldBe JsonNullable.Undefined
            navn shouldBe JsonNullable.Undefined
            fnr shouldBe JsonNullable.Undefined
            enhetsnummer shouldBe JsonNullable.Undefined
        }
        valueToJson(request) shouldBe json
    }

    @Test
    fun `Verdier som er null i JSON blir til Null`() {
        val json = """{"id":null,"navn":null,"fnr":null,"enhetsnummer":null}"""
        val request = jsonToValue<TestRequest>(json)
        assertSoftly(request) {
            id shouldBe JsonNullable.Null
            navn shouldBe JsonNullable.Null
            fnr shouldBe JsonNullable.Null
            enhetsnummer shouldBe JsonNullable.Null
        }
        valueToJson(request) shouldBe json
    }

    @Test
    fun `Verdier som er sett i JSON blir til Present`() {
        val json = """{"id":"$id","navn":"test","fnr":"$fnr","enhetsnummer":"$enhetsnummer"}"""
        val request = jsonToValue<TestRequest>(json)
        assertSoftly(request) {
            id shouldBe JsonNullable.of(this@JsonNullableJacksonTest.id)
            navn shouldBe JsonNullable.of("test")
            fnr shouldBe JsonNullable.of(this@JsonNullableJacksonTest.fnr)
            enhetsnummer shouldBe JsonNullable.of(this@JsonNullableJacksonTest.enhetsnummer)
        }
        valueToJson(request) shouldBe json
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    data class TestRequest(
        val id: JsonNullable<UUID>,
        val navn: JsonNullable<String>,
        val fnr: JsonNullable<Fødselsnummer>,
        val enhetsnummer: JsonNullable<Enhetsnummer>,
    )
}
