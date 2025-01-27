package no.nav.hjelpemidler.database

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlin.math.ceil

interface Page<out T> : Iterable<T> {
    val content: List<T>
    val totalElements: Long
    val pageNumber: Int
    val pageSize: Int
    val totalPages: Int
}

internal data class DefaultPage<out T>(
    override val content: List<T>,
    override val totalElements: Long,
    @JsonIgnore
    val pageRequest: PageRequest,
) : Page<T>, Iterable<T> by content {
    override val pageNumber: Int get() = pageRequest.pageNumber
    override val pageSize: Int get() = pageRequest.pageSize
    override val totalPages: Int get() = ceil(totalElements.toDouble() / pageSize).toInt()
}

fun <T> pageOf(
    content: List<T>,
    totalElements: Long,
    pageRequest: PageRequest,
): Page<T> = DefaultPage(content, totalElements, pageRequest)

fun <T> emptyPage(pageRequest: PageRequest = PageRequest()): Page<T> = pageOf(emptyList(), 0, pageRequest)
