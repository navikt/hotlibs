package no.nav.hjelpemidler.domain.serialization

import java.util.Optional

sealed interface JsonNullable<out T> {
    data object Undefined : JsonNullable<Nothing>

    data class Present<out T>(val value: T?) : JsonNullable<T> {
        override fun toString(): String = value?.toString() ?: "Null"
    }

    companion object {
        val Null = Present(null)

        fun <T> of(value: T?): JsonNullable<T> = Present(value)
    }
}

fun <T> JsonNullable<T>?.asOptional(): Optional<T & Any> = if (this is JsonNullable.Present) {
    Optional.ofNullable<T>(value)
} else {
    Optional.empty<T>()
}
