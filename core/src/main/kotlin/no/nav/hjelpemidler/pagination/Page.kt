package no.nav.hjelpemidler.pagination

import com.fasterxml.jackson.annotation.JsonIgnore

interface Page<out T> : Pageable<T> {
    val content: List<T>

    override fun iterator(): Iterator<T> = content.iterator()
}

internal data class DefaultPage<out T>(
    override val content: List<T>,
    override val totalElements: Long,
    @JsonIgnore
    val pageRequest: PageRequest,
) : Page<T>, PageRequest by pageRequest

fun <T> pageOf(content: List<T>, totalElements: Long, pageRequest: PageRequest): Page<T> =
    DefaultPage(content, totalElements, pageRequest)

fun <T> emptyPage(pageRequest: PageRequest = DefaultPageRequest()): Page<T> =
    pageOf(emptyList(), totalElements = 0, pageRequest)
