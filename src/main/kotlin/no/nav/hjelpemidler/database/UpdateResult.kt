package no.nav.hjelpemidler.database

data class UpdateResult(
    val rowCount: Int? = null,
    val generatedId: Long? = null,
) {
    fun validate() = check(rowCount != 0) {
        "rowCount var 0"
    }
}
