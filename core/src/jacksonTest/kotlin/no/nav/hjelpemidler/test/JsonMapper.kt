package no.nav.hjelpemidler.test

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.readValue
import org.intellij.lang.annotations.Language

val jsonMapper: JsonMapper = jacksonMapperBuilder().build()

fun valueToJson(value: Any?): String = jsonMapper.writeValueAsString(value)
inline fun <reified T> jsonToValue(@Language("JSON") value: String): T = jsonMapper.readValue<T>(value)
