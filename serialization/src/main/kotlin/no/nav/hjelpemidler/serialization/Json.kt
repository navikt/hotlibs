package no.nav.hjelpemidler.serialization

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import org.intellij.lang.annotations.Language

@JvmInline
value class Json(@Language("JSON") private val value: String) : CharSequence by value, Comparable<Json> {
    fun toTree(): JsonNode = jsonMapper.readTree(value)
    inline fun <reified T> toValue(): T = jsonMapper.readValue<T>(toString())
    override fun compareTo(other: Json): Int = value.compareTo(other.value)
    override fun toString(): String = value
}

fun String.toJson(): Json = Json(this)

@JvmName("toJsonOrNull")
fun String?.toJson(): Json? = this?.toJson()
