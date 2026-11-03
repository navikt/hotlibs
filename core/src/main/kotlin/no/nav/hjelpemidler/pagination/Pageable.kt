package no.nav.hjelpemidler.pagination

/**
 * A page of results. [pageNumber] is 1-based.
 * An empty result is treated as a single empty page.
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
