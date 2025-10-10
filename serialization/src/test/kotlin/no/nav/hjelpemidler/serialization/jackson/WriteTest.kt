package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.TextNode
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class WriteTest {
    @Test
    fun `null til JsonNode`() {
        valueToTree(null).shouldBeInstanceOf<NullNode>()
    }

    @Test
    fun `String til JsonNode`() {
        valueToTree("test").shouldBeInstanceOf<TextNode>()
    }
}
