package no.nav.hjelpemidler.core

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
sealed class Maybe<out T> {
    inline fun filter(predicate: (T) -> Boolean): Maybe<T> = flatMap { value ->
        if (predicate(value)) Present(value) else Absent
    }

    inline fun filterNot(predicate: (T) -> Boolean): Maybe<T> = flatMap { value ->
        if (predicate(value)) Absent else Present(value)
    }

    fun filterNotNull(): Maybe<T & Any> = flatMap { value ->
        if (value == null) Absent else Present(value)
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
        if (this is Absent) block()
    }

    inline fun onAbsent(block: () -> Unit): Maybe<T> = also {
        if (it is Absent) block()
    }

    inline fun ifPresent(block: (T) -> Unit) {
        if (this is Present) block(value)
    }

    inline fun onPresent(block: (T) -> Unit): Maybe<T> = also {
        if (it is Present) block(it.value)
    }

    data object Absent : Maybe<Nothing>()

    data class Present<out T>(val value: T) : Maybe<T>()

    companion object {
        val Null: Maybe<Nothing?> = Present(null)

        operator fun <T> invoke(value: T): Maybe<T> = Present(value)
    }
}
