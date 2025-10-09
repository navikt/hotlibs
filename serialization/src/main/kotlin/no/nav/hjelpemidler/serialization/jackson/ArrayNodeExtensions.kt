package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.node.ArrayNode
import java.math.BigDecimal
import java.math.BigInteger
import java.time.temporal.Temporal

fun ArrayNode.add(value: Any?): ArrayNode = when (value) {
    null -> addNull()
    is BigDecimal -> add(value)
    is BigInteger -> add(value)
    is Boolean -> add(value)
    is ByteArray -> add(value)
    is Double -> add(value)
    is Float -> add(value)
    is Int -> add(value)
    is Long -> add(value)
    is Short -> add(value)
    is String -> add(value)
    is Temporal -> add(value.toString())
    else -> throw IllegalArgumentException("Invalid value type: ${value::class}")
}
