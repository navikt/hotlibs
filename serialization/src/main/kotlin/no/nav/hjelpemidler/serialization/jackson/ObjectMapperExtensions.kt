package no.nav.hjelpemidler.serialization.jackson

import no.nav.hjelpemidler.io.useResourceAsStream
import tools.jackson.databind.JavaType
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.node.MissingNode
import tools.jackson.databind.node.NullNode
import tools.jackson.module.kotlin.readValue
import java.nio.file.Path
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType
import kotlin.reflect.typeOf

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

fun ObjectMapper.constructType(type: KType): JavaType =
    constructType(type.javaType)

inline fun <reified T> ObjectMapper.constructType(): JavaType =
    constructType(typeOf<T>())
