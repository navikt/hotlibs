package no.nav.hjelpemidler.pagination

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

interface PageRequest {
    val pageNumber: Int
    val pageSize: Int

    val limit: Int @JsonIgnore get() = pageSize
    val offset: Int @JsonIgnore get() = (pageNumber - 1) * pageSize

    fun next(): PageRequest = DefaultPageRequest(pageNumber + 1, pageSize)

    companion object {
        val ALL: PageRequest = DefaultPageRequest(pageNumber = 1, pageSize = Int.MAX_VALUE)

        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        operator fun invoke(
            @JsonProperty("pageNumber")
            pageNumber: Int = 1,
            @JsonProperty("pageSize")
            pageSize: Int = 50,
        ): PageRequest = DefaultPageRequest(pageNumber, pageSize)
    }
}

internal data class DefaultPageRequest(
    override val pageNumber: Int = 1,
    override val pageSize: Int = 50,
) : PageRequest {
    init {
        require(pageNumber > 0) { "pageNumber: $pageNumber er ugyldig, må være positiv" }
        require(pageSize > 0) { "pageSize: $pageSize er ugyldig, må være positiv" }
    }

    override fun toString(): String = "$pageNumber/$pageSize"
}
