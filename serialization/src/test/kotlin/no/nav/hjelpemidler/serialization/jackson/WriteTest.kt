package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.TextNode
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

class WriteTest {
    @Test
    fun `null til JsonNode`() {
        (null as Any?).toTree().shouldBeInstanceOf<NullNode>()
    }

    @Test
    fun `String til JsonNode`() {
        "test".toTree().shouldBeInstanceOf<TextNode>()
    }
}
