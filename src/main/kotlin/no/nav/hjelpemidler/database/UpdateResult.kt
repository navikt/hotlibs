package no.nav.hjelpemidler.database

data class UpdateResult(val actualRowCount: Int? = null) {
    fun expect(expectedRowCount: Int = 1) =
        check(actualRowCount == expectedRowCount) {
            "$actualRowCount != $expectedRowCount"
        }
}
