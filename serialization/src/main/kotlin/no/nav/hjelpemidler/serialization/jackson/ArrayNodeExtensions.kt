package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.node.ArrayNode
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.temporal.Temporal
import java.util.UUID

fun ArrayNode.add(value: Any?): ArrayNode = when (value) {
    null -> addNull()
    is BigDecimal -> add(value)
    is BigInteger -> add(value)
    is Boolean -> add(value)
    is ByteArray -> add(value)
    is Date -> add(value.toLocalDate().toString())
    is Double -> add(value)
    is Float -> add(value)
    is Int -> add(value)
    is Long -> add(value)
    is Short -> add(value)
    is String -> add(value)
    is Temporal -> add(value.toString())
    is Time -> add(value.toLocalTime().toString())
    is Timestamp -> add(value.toLocalDateTime().toString())
    is UUID -> add(value.toString())
    else -> throw IllegalArgumentException("Invalid value type: ${value::class}")
}
