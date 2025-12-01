package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.treeToValue
import org.intellij.lang.annotations.Language
import kotlin.reflect.KClass

fun <T : Any> treeToValue(node: JsonNode, type: KClass<T>): T = jsonMapper.treeToValue<T>(node, type.java)

fun <T> treeToValue(node: JsonNode, type: TypeReference<T>): T = jsonMapper.treeToValue<T>(node, type)

inline fun <reified T> treeToValue(node: JsonNode): T = jsonMapper.treeToValue<T>(node)

fun <T : Any> treeToValueOrNull(node: JsonNode?, type: KClass<T>): T? =
    if (node == null || node.isMissingOrNull) null else jsonMapper.treeToValue<T>(node, type.java)

fun <T> treeToValueOrNull(node: JsonNode?, type: TypeReference<T>): T? =
    if (node == null || node.isMissingOrNull) null else jsonMapper.treeToValue<T>(node, type)

inline fun <reified T> treeToValueOrNull(node: JsonNode?): T? =
    if (node == null || node.isMissingOrNull) null else jsonMapper.treeToValue<T>(node)

fun <T : Any> jsonToValue(@Language("JSON") value: String, type: KClass<T>): T =
    jsonMapper.readValue<T>(value, type.java)

fun <T> jsonToValue(@Language("JSON") value: String, type: TypeReference<T>): T = jsonMapper.readValue<T>(value, type)

inline fun <reified T> jsonToValue(@Language("JSON") value: String): T = jsonMapper.readValue<T>(value)

fun jsonResourceToTree(name: String): JsonNode = jsonMapper.readResourceAsTree(name)

inline fun <reified T> jsonResourceToValue(name: String): T = jsonMapper.readResourceAsValue<T>(name)

fun jsonToTree(@Language("JSON") value: String): JsonNode = jsonMapper.readTree(value)

fun jsonToTreeOrNull(@Language("JSON") value: String): JsonNode? = jsonMapper.readTree(value).orNull()
