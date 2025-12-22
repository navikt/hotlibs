package no.nav.hjelpemidler.serialization.jackson

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

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

    @Test
    fun `Skal opprette JavaType fra KType`() {
        val type = jsonMapper.constructType<List<String>>()
        val elements = jsonMapper.readValue<Any?>("""["a","b","c"]""", type)
        elements.shouldBeInstanceOf<List<String>>().shouldContainExactly("a", "b", "c")
    }
}

private data class Message(val value: String)
