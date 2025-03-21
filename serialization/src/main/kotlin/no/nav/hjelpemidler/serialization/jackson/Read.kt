package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.treeToValue
import org.intellij.lang.annotations.Language

inline fun <reified T : Any> treeToValue(node: JsonNode): T =
    jsonMapper.treeToValue<T>(node)

inline fun <reified T> treeToValueOrNull(node: JsonNode?): T? =
    if (node == null || node.isMissingOrNull) null else jsonMapper.treeToValue<T>(node)

fun jsonToTree(@Language("JSON") value: String): JsonNode =
    jsonMapper.readTree(value)

inline fun <reified T> jsonToValue(@Language("JSON") value: String): T =
    jsonMapper.readValue<T>(value)

fun jsonResourceToTree(name: String): JsonNode =
    jsonMapper.readResourceAsTree(name)

inline fun <reified T> jsonResourceToValue(name: String): T =
    jsonMapper.readResourceAsValue<T>(name)
