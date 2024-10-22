package no.nav.hjelpemidler.database

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder

interface JsonMapper {
    fun <T> convertValue(fromValue: Any?, toValueTypeRef: TypeReference<T>): T
    fun <T> readValue(content: String?, valueTypeRef: TypeReference<T>): T
    fun <T> readValue(src: ByteArray?, valueTypeRef: TypeReference<T>): T
    fun <T> writeValueAsString(value: T): String
    fun <T> writeValueAsStringOrNull(value: T): String? = when (value) {
        null, is NullNode, is MissingNode -> null
        else -> writeValueAsString(value)
    }
}

internal class DefaultJsonMapper : JsonMapper {
    private val wrapped = jacksonMapperBuilder()
        .addModule(JavaTimeModule())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build()

    override fun <T> convertValue(fromValue: Any?, toValueTypeRef: TypeReference<T>): T =
        wrapped.convertValue(fromValue, toValueTypeRef)

    override fun <T> readValue(content: String?, valueTypeRef: TypeReference<T>): T =
        wrapped.readValue(content, valueTypeRef)

    override fun <T> readValue(src: ByteArray?, valueTypeRef: TypeReference<T>): T =
        wrapped.readValue(src, valueTypeRef)

    override fun <T> writeValueAsString(value: T): String =
        wrapped.writeValueAsString(value)
}

val jsonMapper: JsonMapper by service<JsonMapper>(::DefaultJsonMapper)
