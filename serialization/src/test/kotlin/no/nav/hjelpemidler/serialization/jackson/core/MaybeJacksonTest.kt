package no.nav.hjelpemidler.serialization.jackson.core

import com.fasterxml.jackson.annotation.JsonInclude
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.domain.Maybe
import no.nav.hjelpemidler.domain.enhet.Enhet
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.domain.id.StringId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import no.nav.hjelpemidler.text.doubleQuoted
import java.util.UUID
import kotlin.test.Test

class MaybeJacksonTest {
    private val id = UUID.randomUUID()
    private val fnr = Fødselsnummer(40.år)
    private val enhetsnummer = Enhet.IT_AVDELINGEN.nummer

    @Test
    fun `Verdier som mangler i JSON blir til Absent`() {
        val json = "{}"
        val request = jsonToValue<TestRequest>(json)
        assertSoftly(request) {
            id shouldBe Maybe.Absent
            navn shouldBe Maybe.Absent
            fnr shouldBe Maybe.Absent
            enhetsnummer shouldBe Maybe.Absent
        }
        valueToJson(request) shouldBe json
    }

    @Test
    fun `Verdier som er null i JSON blir til Null`() {
        val json = """{"id":null,"navn":null,"fnr":null,"enhetsnummer":null}"""
        val request = jsonToValue<TestRequest>(json)
        assertSoftly(request) {
            id shouldBe Maybe.Null
            navn shouldBe Maybe.Null
            fnr shouldBe Maybe.Null
            enhetsnummer shouldBe Maybe.Null
        }
        valueToJson(request) shouldBe json
    }

    @Test
    fun `Verdier som er sett i JSON blir til Present`() {
        val json = """{"id":"$id","navn":"test","fnr":"$fnr","enhetsnummer":"$enhetsnummer"}"""
        val request = jsonToValue<TestRequest>(json)
        assertSoftly(request) {
            id shouldBe Maybe(this@MaybeJacksonTest.id)
            navn shouldBe Maybe("test")
            fnr shouldBe Maybe(this@MaybeJacksonTest.fnr)
            enhetsnummer shouldBe Maybe(this@MaybeJacksonTest.enhetsnummer)
        }
        valueToJson(request) shouldBe json
    }

    @Test
    fun `Root value`() {
        valueToJson(Maybe.Absent) shouldBe "null"
        valueToJson(Maybe.Null) shouldBe "null"
        valueToJson(Maybe("test")) shouldBe "test".doubleQuoted()

        valueToJson(Maybe(fnr)) shouldBe fnr.toString().doubleQuoted()
        valueToJson(Maybe(object : StringId("1000") {})) shouldBe "1000".doubleQuoted()
        valueToJson(Maybe(id)) shouldBe id.toString().doubleQuoted()
    }

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    data class TestRequest(
        val id: Maybe<UUID>,
        val navn: Maybe<String>,
        val fnr: Maybe<Fødselsnummer>,
        val enhetsnummer: Maybe<Enhetsnummer>,
    )
}
