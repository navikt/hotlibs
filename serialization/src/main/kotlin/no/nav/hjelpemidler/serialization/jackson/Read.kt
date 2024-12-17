package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue

fun readTree(content: String): JsonNode = jsonMapper.readTree(content)

inline fun <reified T> readValue(content: String): T = jsonMapper.readValue<T>(content)

inline fun <reified T> readResource(name: String): T = jsonMapper.readResource<T>(name)
