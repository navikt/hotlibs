package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode
import no.nav.hjelpemidler.serialization.Json
import org.intellij.lang.annotations.Language

fun jsonToTree(@Language("JSON") content: String): JsonNode = Json(content).toTree()

inline fun <reified T> jsonToValue(@Language("JSON") content: String): T = Json(content).toValue<T>()

fun jsonResourceToTree(name: String): JsonNode = jsonMapper.readResourceAsTree(name)

inline fun <reified T> jsonResourceToValue(name: String): T = jsonMapper.readResourceAsValue<T>(name)
