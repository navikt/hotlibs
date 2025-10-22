package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectWriter

fun valueToTree(value: Any?): JsonNode = jsonMapper.valueToTree(value)

private val writer: ObjectWriter by lazy { jsonMapper.writerWithDefaultPrettyPrinter() }
fun valueToJson(value: Any?, pretty: Boolean = false): String = if (pretty) {
    writer.writeValueAsString(value)
} else {
    jsonMapper.writeValueAsString(value)
}
