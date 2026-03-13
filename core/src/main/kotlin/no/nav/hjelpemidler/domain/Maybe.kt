package no.nav.hjelpemidler.domain

sealed interface Maybe<out T> {
    val orNull: T?
        get() = when (this) {
            Absent -> null
            is Present -> value
        }

    fun <R> map(transform: (T?) -> R?): Maybe<R> = flatMap { Present(transform(it)) }

    fun <R> flatMap(transform: (T?) -> Maybe<R>): Maybe<R> = when (this) {
        Absent -> Absent
        is Present -> transform(value)
    }

    data object Absent : Maybe<Nothing>
    data class Present<out T>(val value: T?) : Maybe<T>

    companion object {
        val Null: Present<Nothing> = Present(null)

        operator fun <T> invoke(value: T?): Present<T> = Present<T>(value)
    }
}
