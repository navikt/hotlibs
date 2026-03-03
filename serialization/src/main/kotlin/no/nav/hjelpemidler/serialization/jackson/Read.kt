package no.nav.hjelpemidler.serialization.jackson

import org.intellij.lang.annotations.Language
import tools.jackson.databind.JsonNode
import tools.jackson.module.kotlin.readValue
import tools.jackson.module.kotlin.treeToValue

inline fun <reified T> treeToValue(node: JsonNode): T =
    jsonMapper.treeToValue<T>(node)

inline fun <reified T> treeToValueOrNull(node: JsonNode?): T? =
    if (node == null || node.isMissingOrNull) null else jsonMapper.treeToValue<T>(node)

inline fun <reified T> jsonToValue(@Language("JSON") value: String): T =
    jsonMapper.readValue<T>(value)

fun jsonResourceToTree(name: String): JsonNode =
    jsonMapper.readResourceAsTree(name)

inline fun <reified T> jsonResourceToValue(name: String): T =
    jsonMapper.readResourceAsValue<T>(name)

fun jsonToTree(@Language("JSON") value: String): JsonNode =
    jsonMapper.readTree(value)

fun jsonToTreeOrNull(@Language("JSON") value: String): JsonNode? =
    jsonMapper.readTree(value).orNull()
