package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode

fun valueToTree(value: Any?): JsonNode = jsonMapper.valueToTree(value)

fun valueToJson(value: Any?, pretty: Boolean = false): String =
    if (pretty) {
        jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value)
    } else {
        jsonMapper.writeValueAsString(value)
    }

fun Any?.toTree(): JsonNode = valueToTree(this)

fun Any?.toJson(pretty: Boolean = false): String = valueToJson(this, pretty)
