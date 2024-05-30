package no.nav.hjelpemidler.database

data class Page<T>(
    val content: List<T>,
    val totalElements: Long,
    val pageRequest: PageRequest,
) : List<T> by content

fun <T> emptyPage(pageRequest: PageRequest = PageRequest(1)): Page<T> =
    Page(emptyList(), 0, pageRequest)
