package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode
import org.intellij.lang.annotations.Language

@JvmInline
value class Json(@param:Language("JSON") private val value: String) : Comparable<Json>, CharSequence by value {
    fun toTree(): JsonNode = jsonToTree(value)
    inline fun <reified T> toValue(): T = jsonToValue<T>(toString())

    override fun compareTo(other: Json): Int = value.compareTo(other.value)
    override fun toString(): String = value
}
