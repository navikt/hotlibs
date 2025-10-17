package no.nav.hjelpemidler.database

import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.database.test.testDataSource
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import kotlin.test.Ignore
import kotlin.test.Test

class OracleTest {
    @Test
    @Ignore
    fun `test 1`() = runTest {
        transactionAsync(testDataSource) {
            it.update(
                """
                    INSERT INTO test (fnr) VALUES (:fnr)
                """.trimIndent(),
                Fødselsnummer(30.år).toQueryParameters("fnr"),
            )
        }
        transactionAsync(testDataSource) {
            val value = it.single("""SELECT "DATE" FROM test WHERE id = 1""") { row ->
                row.localDate(1)
            }
            println(value)
        }
    }
}
