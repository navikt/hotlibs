package no.nav.hjelpemidler.pagination

interface Pageable<out T> : Iterable<T> {
    val pageNumber: Int
    val pageSize: Int
    val totalElements: Long
    val totalPages: Int
        get() = when {
            pageSize <= 0 -> 1
            totalElements <= 0 -> 0
            else -> ((totalElements - 1) / pageSize + 1).toInt()
        }
}
