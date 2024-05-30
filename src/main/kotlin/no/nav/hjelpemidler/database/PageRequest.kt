package no.nav.hjelpemidler.database

data class PageRequest(
    val pageNumber: Int,
    val pageSize: Int = 20,
) {
    init {
        check(pageNumber > 0) { "pageNumber: $pageNumber er ugyldig, må være positiv" }
        check(pageSize > 0) { "pageSize: $pageSize er ugyldig, må være positiv" }
    }

    val limit: Int get() = pageSize
    val offset: Int get() = (pageNumber - 1) * pageSize
}
