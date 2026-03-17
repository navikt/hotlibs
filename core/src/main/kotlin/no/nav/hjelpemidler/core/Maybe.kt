package no.nav.hjelpemidler.core

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Gjør det mulig å skille mellom `null` og `undefined` i f.eks. HTTP PATCH.
 *
 * NB! Husk å legge til [com.fasterxml.jackson.annotation.JsonInclude] på request-klasse:
 * ```kotlin
 * @JsonInclude(JsonInclude.Include.NON_EMPTY)
 * data class PatchRequest(val value: Maybe<String> = Maybe.Absent)
 * ```
 * Dette vil gjøre at [Maybe.Absent] ikke blir med i JSON ved serialisering, mens `Maybe.Present(null)` blir det.
 */
@OptIn(ExperimentalContracts::class)
sealed class Maybe<out T> {
    fun isAbsent(): Boolean {
        contract {
            returns(true) implies (this@Maybe is Absent)
            returns(false) implies (this@Maybe is Present)
        }
        return this is Absent
    }

    fun isPresent(): Boolean {
        contract {
            returns(false) implies (this@Maybe is Absent)
            returns(true) implies (this@Maybe is Present)
        }
        return this is Present
    }

    fun filterNotNull(): Maybe<T & Any> = when (this) {
        is Absent -> this
        is Present -> if (value == null) Absent else Present(value)
    }

    fun getOrNull(): T? = when (this) {
        is Absent -> null
        is Present -> value
    }

    inline fun <R> map(transform: (T) -> R): Maybe<R> = flatMap {
        Present(transform(it))
    }

    inline fun <R> flatMap(transform: (T) -> Maybe<R>): Maybe<R> = when (this) {
        is Absent -> this
        is Present -> transform(value)
    }

    inline fun ifAbsent(block: () -> Unit) {
        if (isAbsent()) block()
    }

    inline fun onAbsent(block: () -> Unit): Maybe<T> = also {
        if (it.isAbsent()) block()
    }

    inline fun ifPresent(block: (T) -> Unit) {
        if (isPresent()) block(value)
    }

    inline fun onPresent(block: (T) -> Unit): Maybe<T> = also {
        if (it.isPresent()) block(it.value)
    }

    data object Absent : Maybe<Nothing>()

    data class Present<out T>(val value: T) : Maybe<T>()

    companion object {
        val Null: Present<Nothing?> = Present(null)

        operator fun <T> invoke(value: T): Present<T> = Present(value)
    }
}
