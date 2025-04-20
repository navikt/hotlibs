package no.nav.hjelpemidler.domain.kodeverk

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.test.testFactory
import org.junit.jupiter.api.TestFactory

class KodeverkDeserializerTest {
    private val jsonMapper = jacksonMapperBuilder()
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .build()

    @TestFactory
    fun tilJson() = testFactory(
        sequenceOf(
            TestEnum.OPPRETTET to "OPPRETTET",
            UkjentKode<TestEnum>("FEILREGISTRERT") to "FEILREGISTRERT",
            UkjentKode<TestEnum>("Feilregistrert") to "Feilregistrert",
        ),
        { (testEnum, testEnumJson) -> """$testEnum -> "$testEnumJson"""" },
    ) { (testEnum, testEnumJson) ->
        val data = TestData(testEnum)
        jsonMapper.writeValueAsString(data) shouldBe """{"testEnum":"$testEnumJson"}"""
    }

    @TestFactory
    fun fraJson() = testFactory(
        sequenceOf(
            "OPPRETTET" to TestEnum.OPPRETTET,
            "Opprettet" to TestEnum.OPPRETTET,
            "MOTTATT" to TestEnum.OPPRETTET,
            "Mottatt" to TestEnum.OPPRETTET,
            "BEHANDLET" to TestEnum.FERDIGSTILT,
            "Behandlet" to TestEnum.FERDIGSTILT,
            "FEILREGISTRERT" to UkjentKode<TestEnum>("FEILREGISTRERT"),
            "Feilregistrert" to UkjentKode<TestEnum>("Feilregistrert"),
        ),
        { (testEnumJson, testEnum) -> """"$testEnumJson" -> $testEnum""" },
    ) { (testEnumJson, testEnum) ->
        val data = jsonMapper.readValue<TestData>("""{"testEnum":"$testEnumJson"}""")
        data.testEnum shouldBe testEnum
    }

    private enum class TestEnum : Kodeverk<TestEnum> {
        @JsonAlias("MOTTATT")
        OPPRETTET,

        UNDER_BEHANDLING,

        @JsonProperty("BEHANDLET")
        FERDIGSTILT,
    }

    private data class TestData(
        val testEnum: Kodeverk<TestEnum>,
    )
}
