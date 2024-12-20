package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import org.intellij.lang.annotations.Language

fun jsonToTree(@Language("JSON") content: String): JsonNode = jsonMapper.readTree(content)

inline fun <reified T> jsonToValue(@Language("JSON") content: String): T = jsonMapper.readValue<T>(content)

fun jsonResourceToTree(name: String): JsonNode = jsonMapper.readResourceAsTree(name)

inline fun <reified T> jsonResourceToValue(name: String): T = jsonMapper.readResourceAsValue<T>(name)
