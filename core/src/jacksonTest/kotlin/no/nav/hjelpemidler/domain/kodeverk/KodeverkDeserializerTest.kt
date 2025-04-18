package no.nav.hjelpemidler.domain.kodeverk

import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.test.jsonMapper
import kotlin.test.Test

class KodeverkDeserializerTest {
    @Test
    fun `Til JSON`() {
        var data = TestData(TestEnum.A)
        jsonMapper.writeValueAsString(data) shouldBe """{"testEnum":"A"}"""
        data = TestData(UkjentKode("D"))
        jsonMapper.writeValueAsString(data) shouldBe """{"testEnum":"D"}"""
    }

    @Test
    fun `Fra JSON`() {
        var data = jsonMapper.readValue<TestData>("""{"testEnum":"A"}""")
        data.testEnum shouldBe TestEnum.A
        data = jsonMapper.readValue<TestData>("""{"testEnum":"D"}""")
        data.testEnum shouldBe UkjentKode("D")
    }
}

private enum class TestEnum : Kodeverk<TestEnum> {
    A, B, C
}

private data class TestData(
    val testEnum: Kodeverk<TestEnum>,
)
