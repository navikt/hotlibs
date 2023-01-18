package no.nav.hjelpemidler.database.test

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import no.nav.hjelpemidler.database.JsonMapper

class TestJsonMapper : JsonMapper {
    private val wrapped = jacksonMapperBuilder().build()

    override fun <T> writeValueAsString(value: T): String =
        wrapped.writeValueAsString(value)

    override fun <T> readValue(content: String?, valueTypeRef: TypeReference<T>): T =
        wrapped.readValue(content, valueTypeRef)

    override fun <T> readValue(src: ByteArray?, valueTypeRef: TypeReference<T>): T =
        wrapped.readValue(src, valueTypeRef)

    override fun <T> convertValue(fromValue: Any?, toValueTypeRef: TypeReference<T>): T =
        wrapped.convertValue(fromValue, toValueTypeRef)
}
