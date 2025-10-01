package no.nav.hjelpemidler.domain.id

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import kotlin.test.Test

class IdJacksonTest {
    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(numberId) shouldBe numberIdJsonString
        valueToJson(stringId) shouldBe stringIdJsonString
        valueToJson(uuidId) shouldBe uuidIdJsonString
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<TestLongId>(numberIdJsonNumber) shouldBe numberId
        jsonToValue<TestLongId>(numberIdJsonString) shouldBe numberId
        jsonToValue<TestStringId>(stringIdJsonString) shouldBe stringId
        jsonToValue<TestUuidId>(uuidIdJsonString) shouldBe uuidId
    }
}
