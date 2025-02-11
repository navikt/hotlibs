package no.nav.hjelpemidler.nare.core

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

abstract class Node<T : Node<T>>(
    val beskrivelse: String,
    @get:JsonAlias("identifikator")
    val id: String,
    @get:JsonInclude(Include.NON_EMPTY)
    val barn: List<T>,
) : LogiskOperand<T> {
    abstract fun med(beskrivelse: String, id: String): T

    protected fun toList(): List<T> =
        if (id.isBlank() && barn.isNotEmpty()) {
            barn
        } else {
            @Suppress("UNCHECKED_CAST")
            listOf(this as T)
        }

    override fun toString(): String = "'$beskrivelse' (id: '$id')"
}
