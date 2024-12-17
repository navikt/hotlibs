package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ObjectMapperExtensionsTest {
    @Test
    fun `Skal lese resource som JsonNode`() {
        val node = jsonMapper.readResource<JsonNode>("/test.json")

        node["message"].textValue() shouldBe "Hello, world!"
    }
}
