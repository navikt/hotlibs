package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import java.math.BigDecimal
import java.math.BigInteger
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount
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

    // java.time.*
    is Temporal -> textNode(value.toString())
    is TemporalAmount -> textNode(value.toString())

    // java.sql.*
    is java.sql.Array -> node(value.array)
    is java.sql.Blob -> value.binaryStream.use { binaryNode(it.readBytes()) }
    is java.sql.Clob -> value.characterStream.use { textNode(it.readText()) }
    is java.sql.Date -> textNode(value.toLocalDate().toString())
    is java.sql.Time -> textNode(value.toLocalTime().toString())
    is java.sql.Timestamp -> textNode(value.toLocalDateTime().toString())

    is Array<*> -> {
        val node = arrayNode(value.size)
        value.forEach { node.add(node(it)) }
        node
    }

    is ByteArray -> binaryNode(value)

    else -> throw IllegalArgumentException("Invalid node type: ${value::class}")
}
