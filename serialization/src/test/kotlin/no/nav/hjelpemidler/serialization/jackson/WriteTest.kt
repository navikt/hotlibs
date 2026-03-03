package no.nav.hjelpemidler.serialization.jackson

import io.kotest.matchers.types.shouldBeInstanceOf
import tools.jackson.databind.node.NullNode
import tools.jackson.databind.node.StringNode
import kotlin.test.Test

class WriteTest {
    @Test
    fun `null til JsonNode`() {
        valueToTree(null).shouldBeInstanceOf<NullNode>()
    }

    @Test
    fun `String til JsonNode`() {
        valueToTree("test").shouldBeInstanceOf<StringNode>()
    }
}
