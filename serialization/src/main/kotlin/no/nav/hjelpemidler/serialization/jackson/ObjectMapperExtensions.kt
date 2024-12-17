package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.module.kotlin.readValue
import java.nio.file.Path

fun ObjectMapper.readResourceAsTree(name: String): JsonNode =
    requireNotNull(this::class.java.getResourceAsStream(name)) { "Fant ikke resource: '$name'" }.use {
        readTree(it)
    }

inline fun <reified T> ObjectMapper.readResourceAsValue(name: String): T =
    requireNotNull(this::class.java.getResourceAsStream(name)) { "Fant ikke resource: '$name'" }.use {
        readValue<T>(it)
    }

fun ObjectMapper.readTree(path: Path): JsonNode = readTree(path.toFile())

inline fun <reified T> ObjectMapper.readValue(path: Path): T = readValue<T>(path.toFile())

fun <T> ObjectMapper.writeValueAsStringOrNull(value: T): String? = when (value) {
    null, is NullNode, is MissingNode -> null
    else -> writeValueAsString(value)
}
