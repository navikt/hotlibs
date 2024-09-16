package no.nav.hjelpemidler.nare.dokumentasjon

import com.fasterxml.jackson.databind.JsonNode
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.nare.test.testJsonMapper
import kotlin.test.Test

class MetadataTest {
    @Test
    fun `Skal benytte navn på egenskap fra interface`() {
        val metadata = DefaultMetadata(
            beskrivelse = "beskrivelse",
            identifikator = "identifikator",
            lovreferanse = "lovreferanse",
            lovdataUrl = "lovdataUrl"
        )

        val jsonNode = testJsonMapper.valueToTree<JsonNode>(metadata)

        jsonNode.path("lovReferanse").textValue() shouldBe metadata.lovreferanse
        jsonNode.path("lovdataLenke").textValue() shouldBe metadata.lovdataUrl
    }
}
