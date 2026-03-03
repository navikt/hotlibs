package no.nav.hjelpemidler.serialization.jackson

import tools.jackson.databind.JsonNode
import tools.jackson.databind.node.JsonNodeFactory
import java.math.BigDecimal
import java.math.BigInteger
import java.time.temporal.Temporal
import java.time.temporal.TemporalAmount
import java.util.UUID

fun JsonNodeFactory.node(value: Any?): JsonNode = when (value) {
    null -> nullNode()

    is JsonNode -> value

    is Boolean -> booleanNode(value)
    is String -> stringNode(value)
    is UUID -> stringNode(value.toString())

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
    is Temporal -> stringNode(value.toString())
    is TemporalAmount -> stringNode(value.toString())

    // java.sql.*
    is java.sql.Array -> node(value.array)
    is java.sql.Blob -> value.binaryStream.use { binaryNode(it.readBytes()) }
    is java.sql.Clob -> value.characterStream.use { stringNode(it.readText()) }
    is java.sql.Date -> stringNode(value.toLocalDate().toString())
    is java.sql.Time -> stringNode(value.toLocalTime().toString())
    is java.sql.Timestamp -> stringNode(value.toLocalDateTime().toString())

    is Array<*> -> {
        val node = arrayNode(value.size)
        value.forEach { node.add(node(it)) }
        node
    }

    is ByteArray -> binaryNode(value)

    else -> throw IllegalArgumentException("Invalid node type: ${value::class}")
}
