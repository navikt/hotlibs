package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.hjelpemidler.io.useResourceAsStream
import java.nio.file.Path

fun ObjectMapper.readResourceAsTree(name: String): JsonNode =
    this::class.useResourceAsStream<JsonNode>(name, ::readTree)

inline fun <reified T> ObjectMapper.readResourceAsValue(name: String): T =
    this::class.useResourceAsStream(name, ::readValue)

fun ObjectMapper.readTree(path: Path): JsonNode = readTree(path.toFile())

inline fun <reified T> ObjectMapper.readValue(path: Path): T = readValue<T>(path.toFile())

fun <T> ObjectMapper.writeValueAsStringOrNull(value: T): String? = when (value) {
    null, is NullNode, is MissingNode -> null
    else -> writeValueAsString(value)
}

inline fun <reified T> ObjectMapper.constructType(): JavaType =
    constructType(jacksonTypeRef<T>())

inline fun <reified T> ObjectMapper.introspectForSerialization(): BeanDescription =
    serializationConfig.introspect(constructType<T>())

inline fun <reified T> ObjectMapper.introspectForDeserialization(): BeanDescription =
    deserializationConfig.introspect(constructType<T>())

inline fun <reified T> ObjectMapper.introspectForCreation(): BeanDescription =
    deserializationConfig.introspectForCreation(constructType<T>())
