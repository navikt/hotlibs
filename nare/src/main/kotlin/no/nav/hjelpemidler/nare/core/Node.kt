package no.nav.hjelpemidler.nare.core

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnore
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
    protected val beskrivelseQuoted: String
        @JsonIgnore
        get() = beskrivelse.singleQuoted()

    @get:JsonIgnore
    protected abstract val self: T

    fun toList(): List<T> =
        if (id.isBlank() && barn.isNotEmpty()) {
            barn
        } else {
            listOf(self)
        }

    override fun toString(): String = toString(1)

    private fun toString(level: Int): String =
        buildString {
            append(beskrivelseQuoted)
            if (id.isNotBlank()) {
                append(" (id: '$id')")
            }
            if (this@Node is Evaluering<*, *>) {
                append(" -> $resultat(begrunnelse: $begrunnelse)")
            }
            barn.forEach {
                appendLine()
                repeat(level) { append('\t') }
                append(it.toString(level + 1))
            }
        }
}
