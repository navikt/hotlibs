package no.nav.hjelpemidler.domain.person

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.test.jsonToValue
import no.nav.hjelpemidler.test.valueToJson
import kotlin.test.Test

class FødselsnummerJacksonTest {
    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(fnr) shouldBe """"$fnr""""
        valueToJson(Person1(fnr)) shouldBe person1Json
        valueToJson(Person2(null)) shouldBe person2Json
        valueToJson(listOf(fnr)) shouldBe fnrAsArrayElementJson
        valueToJson(mapOf(fnr to true)) shouldBe fnrAsMapKeyJson
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<Fødselsnummer>(fnrJson) shouldBe fnr
        jsonToValue<Person1>(person1Json) shouldBe Person1(fnr)
        jsonToValue<Person2>(person2Json) shouldBe Person2(null)
        jsonToValue<List<Fødselsnummer>>(fnrAsArrayElementJson) shouldBe listOf(fnr)
        jsonToValue<Map<Fødselsnummer, Boolean>>(fnrAsMapKeyJson) shouldBe mapOf(fnr to true)
    }
}
