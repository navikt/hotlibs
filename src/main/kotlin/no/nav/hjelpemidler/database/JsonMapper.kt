package no.nav.hjelpemidler.database

import com.fasterxml.jackson.core.type.TypeReference
import java.util.ServiceLoader

interface JsonMapper {
    fun <T> writeValueAsString(value: T): String
    fun <T> readValue(content: String?, valueTypeRef: TypeReference<T>): T
    fun <T> readValue(src: ByteArray?, valueTypeRef: TypeReference<T>): T
    fun <T> convertValue(fromValue: Any?, toValueTypeRef: TypeReference<T>): T
}

val jsonMapper: JsonMapper = ServiceLoader.load(JsonMapper::class.java).single()
