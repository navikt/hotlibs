package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.node.ObjectNode
import java.math.BigDecimal
import java.math.BigInteger
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.time.temporal.Temporal
import java.util.UUID

fun ObjectNode.put(propertyName: String, value: Any?): ObjectNode = when (value) {
    null -> putNull(propertyName)
    is BigDecimal -> put(propertyName, value)
    is BigInteger -> put(propertyName, value)
    is Boolean -> put(propertyName, value)
    is ByteArray -> put(propertyName, value)
    is Date -> put(propertyName, value.toLocalDate().toString())
    is Double -> put(propertyName, value)
    is Float -> put(propertyName, value)
    is Int -> put(propertyName, value)
    is Long -> put(propertyName, value)
    is Short -> put(propertyName, value)
    is String -> put(propertyName, value)
    is Temporal -> put(propertyName, value.toString())
    is Time -> put(propertyName, value.toLocalTime().toString())
    is Timestamp -> put(propertyName, value.toLocalDateTime().toString())
    is UUID -> put(propertyName, value.toString())
    else -> throw IllegalArgumentException("Invalid value type: ${value::class}")
}
