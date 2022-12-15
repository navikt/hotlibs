package no.nav.hjelpemidler.database

data class Page<T>(
    val items: List<T>,
    val total: Int,
) : List<T> by items
