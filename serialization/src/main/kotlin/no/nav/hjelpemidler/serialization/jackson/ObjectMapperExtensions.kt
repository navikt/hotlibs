package no.nav.hjelpemidler.serialization.jackson

import no.nav.hjelpemidler.io.useResourceAsStream
import tools.jackson.databind.BeanDescription
import tools.jackson.databind.JavaType
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.node.MissingNode
import tools.jackson.databind.node.NullNode
import tools.jackson.module.kotlin.jacksonTypeRef
import tools.jackson.module.kotlin.readValue
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

inline fun <reified T> ObjectMapper.constructType(): JavaType = constructType(jacksonTypeRef<T>())

inline fun <reified T> ObjectMapper.introspectForSerialization(): BeanDescription {
    val type = constructType<T>()
    val introspector = serializationConfig().classIntrospectorInstance()
    val annotatedClass = introspector.introspectClassAnnotations(type)
    return introspector.introspectForSerialization(type, annotatedClass)
}

inline fun <reified T> ObjectMapper.introspectForDeserialization(): BeanDescription {
    val type = constructType<T>()
    val introspector = deserializationConfig().classIntrospectorInstance()
    val annotatedClass = introspector.introspectClassAnnotations(type)
    return introspector.introspectForDeserialization(type, annotatedClass)
}
