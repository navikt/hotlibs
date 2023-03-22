package no.nav.hjelpemidler.database

data class UpdateResult(val actualRowCount: Int? = null) {
    fun expect(expectedRowCount: Int = 1) =
        check(actualRowCount == expectedRowCount) {
            "$actualRowCount != $expectedRowCount"
        }

    operator fun plus(other: UpdateResult): UpdateResult =
        UpdateResult(this.actualRowCount + other.actualRowCount)

    private operator fun Int?.plus(other: Int?): Int? =
        when {
            this == null -> other
            other == null -> this
            else -> this + other
        }
}
