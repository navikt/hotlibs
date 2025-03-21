package no.nav.hjelpemidler.serialization

import com.fasterxml.jackson.databind.JsonNode
import no.nav.hjelpemidler.serialization.jackson.jsonToTree
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import org.intellij.lang.annotations.Language

@JvmInline
value class Json(@Language("JSON") private val value: String) : CharSequence by value, Comparable<Json> {
    override fun compareTo(other: Json): Int = value.compareTo(other.value)
    override fun toString(): String = value

    fun toTree(): JsonNode = jsonToTree(value)
    inline fun <reified T> toValue(): T = jsonToValue<T>(toString())
}
