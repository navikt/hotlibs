package no.nav.hjelpemidler.domain

import no.nav.hjelpemidler.domain.Maybe.Absent
import no.nav.hjelpemidler.domain.Maybe.Present

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
sealed interface Maybe<out T> {
    val isAbsent: Boolean get() = this is Absent
    val isPresent: Boolean get() = this is Present

    val valueOrNull: T?
        get() = when (this) {
            is Absent -> null
            is Present -> value
        }

    data object Absent : Maybe<Nothing>

    data class Present<out T>(val value: T?) : Maybe<T> {
        override fun toString(): String = value.toString()
    }

    companion object {
        val Null: Present<Nothing> = Present(null)

        operator fun <T> invoke(value: T?): Present<T> = Present<T>(value)
    }
}

inline fun <T, R> Maybe<T>.map(transform: (T?) -> R): Maybe<R> = flatMap { Present(transform(it)) }

inline fun <T, R> Maybe<T>.flatMap(transform: (T?) -> Maybe<R>): Maybe<R> = when (this) {
    is Absent -> Absent
    is Present -> transform(value)
}

inline fun <T> Maybe<T>.ifPresent(block: (T?) -> Unit) {
    if (this is Present) block(value)
}
