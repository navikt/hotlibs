package no.nav.hjelpemidler.serialization.jackson

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import tools.jackson.databind.node.ArrayNode
import tools.jackson.databind.node.BooleanNode
import tools.jackson.databind.node.IntNode
import tools.jackson.databind.node.NullNode
import tools.jackson.databind.node.ObjectNode
import tools.jackson.databind.node.StringNode
import kotlin.test.Test

class JsonTest {
    @Test
    fun `Konverter JSON til JsonNode`() {
        Json("null").toTree().shouldBeInstanceOf<NullNode>()
        Json("-1").toTree().shouldBeInstanceOf<IntNode>()
        Json("false").toTree().shouldBeInstanceOf<BooleanNode>()
        Json("\"foobar\"").toTree().shouldBeInstanceOf<StringNode>()
        Json("{}").toTree().shouldBeInstanceOf<ObjectNode>()
        Json("[]").toTree().shouldBeInstanceOf<ArrayNode>()
    }

    @Test
    fun `Konverter JSON til verdi`() {
        Json("null").toValue<String?>().shouldBeNull()
        Json("-1").toValue<Int>().shouldBe(-1)
        Json("false").toValue<Boolean>().shouldBeFalse()
        Json("\"foobar\"").toValue<String>().shouldBe("foobar")
        Json("{}").toValue<Map<String, Any?>>().shouldBeEmpty()
        Json("[]").toValue<List<Any?>>().shouldBeEmpty()
    }
}
