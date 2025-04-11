package no.nav.hjelpemidler.nare.core

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import no.nav.hjelpemidler.nare.evaluering.Evaluering

abstract class Node<T : Node<T>> internal constructor(
    val beskrivelse: String,
    @get:JsonAlias("identifikator")
    val id: String,
    @get:JsonInclude(Include.NON_EMPTY)
    val barn: List<T>,
) : LogiskOperand<T> {
    abstract fun med(beskrivelse: String, id: String): T

    fun toList(): List<T> =
        if (id.isBlank() && barn.isNotEmpty()) {
            barn
        } else {
            @Suppress("UNCHECKED_CAST")
            listOf(this as T)
        }

    override fun toString(): String = toString(1)

    private fun toString(level: Int): String =
        buildString {
            if (this@Node is Evaluering<*>) {
                append("$beskrivelse (id: '$id') -> $resultat(begrunnelse: $begrunnelse)")
            } else {
                append("$beskrivelse (id: '$id')")
            }
            barn.forEach {
                appendLine()
                repeat(level) { append('\t') }
                append(it.toString(level + 1))
            }
        }
}
