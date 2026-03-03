package no.nav.hjelpemidler.serialization.jackson

import tools.jackson.databind.JsonNode
import tools.jackson.databind.ObjectWriter

fun valueToTree(value: Any?): JsonNode = jsonMapper.valueToTree(value)

private val writer: ObjectWriter by lazy { jsonMapper.writerWithDefaultPrettyPrinter() }
fun valueToJson(value: Any?, pretty: Boolean = false): String = if (pretty) {
    writer.writeValueAsString(value)
} else {
    jsonMapper.writeValueAsString(value)
}
