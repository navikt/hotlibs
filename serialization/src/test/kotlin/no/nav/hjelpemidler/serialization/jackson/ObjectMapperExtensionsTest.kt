package no.nav.hjelpemidler.serialization.jackson

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ObjectMapperExtensionsTest {
    @Test
    fun `Skal lese resource som JsonNode`() {
        val node = jsonMapper.readResourceAsTree("/message.json")

        node["value"].textValue() shouldBe "Hello, world!"
    }

    @Test
    fun `Skal lese resource som klasse`() {
        val message = jsonMapper.readResourceAsValue<Message>("/message.json")

        message.value shouldBe "Hello, world!"
    }
}

private data class Message(val value: String)
