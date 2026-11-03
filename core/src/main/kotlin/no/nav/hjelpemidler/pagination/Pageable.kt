package no.nav.hjelpemidler.pagination

/**
 * NB! Et tomt resultat gir [totalPages] = `1`.
 */
interface Pageable<out T> : PageRequest, Iterable<T> {
    val totalElements: Long

    val totalPages: Int
        get() = if (totalElements <= 0 || pageSize <= 0) 1
        else ((totalElements - 1) / pageSize + 1).toInt()

    val isFirst: Boolean get() = pageNumber <= 1
    val isLast: Boolean get() = pageNumber >= totalPages
    val hasPrevious: Boolean get() = !isFirst
    val hasNext: Boolean get() = !isLast
}
