package no.nav.hjelpemidler.database

data class UpdateResult(val actualRowCount: Int) {
    fun expect(expectedRowCount: Int = 1) =
        check(actualRowCount == expectedRowCount) {
            "$actualRowCount != $expectedRowCount"
        }

    operator fun plus(other: UpdateResult): UpdateResult =
        UpdateResult(this.actualRowCount + other.actualRowCount)
}
