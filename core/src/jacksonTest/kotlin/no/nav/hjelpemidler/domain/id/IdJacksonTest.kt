package no.nav.hjelpemidler.domain.id

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import java.util.UUID
import kotlin.test.Test

class IdJacksonTest {
    private val numberId = TestLongId(1000)
    private val stringId = TestStringId("2000")
    private val uuidId = TestUuidId(UUID.randomUUID())

    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(numberId) shouldBe numberId.toJson()
        valueToJson(stringId) shouldBe stringId.toJson()
        valueToJson(uuidId) shouldBe uuidId.toJson()
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<TestLongId>("$numberId") shouldBe numberId
        jsonToValue<TestLongId>(numberId.toJson()) shouldBe numberId
        jsonToValue<TestStringId>(stringId.toJson()) shouldBe stringId
        jsonToValue<TestUuidId>(uuidId.toJson()) shouldBe uuidId
    }
}
