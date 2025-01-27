package no.nav.hjelpemidler.database

import com.fasterxml.jackson.annotation.JsonIgnore

data class PageRequest(
    val pageNumber: Int = 1,
    val pageSize: Int = 25,
) {
    init {
        require(pageNumber > 0) { "pageNumber: $pageNumber er ugyldig, må være positiv" }
        require(pageSize > 0) { "pageSize: $pageSize er ugyldig, må være positiv" }
    }

    val limit: Int @JsonIgnore get() = pageSize
    val offset: Int @JsonIgnore get() = (pageNumber - 1) * pageSize

    companion object {
        val ALL: PageRequest = PageRequest(pageNumber = 1, pageSize = Int.MAX_VALUE)
    }
}
