package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.module.kotlin.readValue
import kotlin.reflect.full.isSubclassOf

inline fun <reified T> ObjectMapper.readResource(name: String): T {
    val inputStream = requireNotNull(this::class.java.getResourceAsStream(name)) {
        "Fant ikke resource: '$name'"
    }
    return inputStream.use {
        if (T::class.isSubclassOf(JsonNode::class)) {
            readTree(it) as T
        } else {
            readValue<T>(it)
        }
    }
}

fun <T> ObjectMapper.writeValueAsStringOrNull(value: T): String? = when (value) {
    null, is NullNode, is MissingNode -> null
    else -> writeValueAsString(value)
}
