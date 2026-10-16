package no.nav.hjelpemidler.domain.geografi
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import kotlin.test.Test
import tools.jackson.module.kotlin.readValue

class VeiadresseTest {
    @Test
    fun `accepts legacy adresse`() {
        val result = jsonMapper.readValue<Veiadresse>(
            """
            {
              "adresse": "Storgata 1",
              "postnummer": "0182",
              "poststed": "Oslo"
            }
            """.trimIndent()
        )

        result.adresselinje1 shouldBe "Storgata 1"
        result.adresselinje2 shouldBe null
        result.adresselinje3 shouldBe null
    }

    @Test
    fun `accepts legacy adresse with multiple lines`() {
        val result = jsonMapper.readValue<Veiadresse>(
            """
            {
              "adresse": "Storgata 1, Bygg B, Etasje 3",
              "postnummer": "0182",
              "poststed": "Oslo"
            }
            """.trimIndent()
        )

        result.adresselinje1 shouldBe "Storgata 1"
        result.adresselinje2 shouldBe "Bygg B"
        result.adresselinje3 shouldBe "Etasje 3"
        result.adresse shouldBe "Storgata 1, Bygg B, Etasje 3"
    }

    @Test
    fun `prefers explicit adresselinje fields over legacy adresse regardless of field order`() {
        val fields = listOf(
            """"adresselinje1": "Storgata 1"""",
            """"adresselinje2": "Bygg B"""",
            """"adresselinje3": "Etasje 3"""",
            """"adresse": "Legacygata 99, B, C"""",
            """"postnummer": "0182"""",
            """"poststed": "Oslo"""",
        )

        for (orderedFields in listOf(fields, fields.reversed())) {
            val json = orderedFields.joinToString(
                separator = ",",
                prefix = "{",
                postfix = "}",
            )
            val result = jsonMapper.readValue<Veiadresse>(json)

            result.adresselinje1 shouldBe "Storgata 1"
            result.adresselinje2 shouldBe "Bygg B"
            result.adresselinje3 shouldBe "Etasje 3"
        }
    }

    @Test
    fun `trims whitespace in legacy adresse`() {
        val result = jsonMapper.readValue<Veiadresse>(
            """
            {
              "adresse": "  Storgata 1  ,  Bygg B ,  Etasje 3  ",
              "postnummer": "0182",
              "poststed": "Oslo"
            }
            """.trimIndent()
        )

        result.adresselinje1 shouldBe "Storgata 1"
        result.adresselinje2 shouldBe "Bygg B"
        result.adresselinje3 shouldBe "Etasje 3"
    }

    @Test
    fun `round trip preserves lines and serializes legacy adresse`() {
        val original = Veiadresse(
            adresselinje1 = "Storgata 1",
            adresselinje2 = "Bygg B",
            adresselinje3 = "Etasje 3",
            postnummer = "0182",
            poststed = "Oslo",
        )

        val json = jsonMapper.writeValueAsString(original)

        jsonMapper.readTree(json)["adresse"].asText() shouldBe
                "Storgata 1, Bygg B, Etasje 3"

        jsonMapper.readValue<Veiadresse>(json) shouldBe original
    }
}