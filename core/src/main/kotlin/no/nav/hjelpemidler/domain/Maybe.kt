package no.nav.hjelpemidler.domain

sealed interface Maybe<out T> {
    val orNull: T? get() = (this as? Present)?.value

    data object Absent : Maybe<Nothing>
    data class Present<out T>(val value: T?) : Maybe<T>

    companion object {
        val Null: Present<Nothing> = Present(null)

        operator fun <T> invoke(value: T?): Present<T> = Present<T>(value)
    }
}
