package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.temporal.Temporal
import java.util.UUID

fun JsonNodeFactory.node(value: Any?): JsonNode = when (value) {
    null -> nullNode()

    is JsonNode -> value

    is Boolean -> booleanNode(value)
    is String -> textNode(value)
    is UUID -> textNode(value.toString())

    // numbers
    is Byte -> numberNode(value)
    is Short -> numberNode(value)
    is Int -> numberNode(value)
    is Long -> numberNode(value)
    is Float -> numberNode(value)
    is Double -> numberNode(value)
    is BigInteger -> numberNode(value)
    is BigDecimal -> numberNode(value)

    // datetime
    is Temporal -> textNode(value.toString())
    is Date -> textNode(value.toLocalDate().toString())
    is Time -> textNode(value.toLocalTime().toString())
    is Timestamp -> textNode(value.toLocalDateTime().toString())

    is Array<*> -> {
        val node = arrayNode(value.size)
        value.forEach { node.add(node(it)) }
        node
    }

    is ByteArray -> binaryNode(value)

    else -> throw IllegalArgumentException("Invalid node type: ${value::class}")
}
