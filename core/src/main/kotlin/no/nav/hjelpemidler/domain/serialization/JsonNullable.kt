package no.nav.hjelpemidler.domain.serialization

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
