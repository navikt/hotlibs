package no.nav.hjelpemidler.database

import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.database.test.migrate
import no.nav.hjelpemidler.database.test.testDataSource
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import kotlin.test.Ignore
import kotlin.test.Test

class OracleTest {
    @Test
    @Ignore
    fun `test 1`() = runTest {
        testDataSource.migrate()
        transaction(testDataSource) {
            it.update(
                """
                    INSERT INTO test (fnr) VALUES (:fnr)
                """.trimIndent(),
                Fødselsnummer(30.år).toQueryParameters("fnr"),
            )
        }
        transaction(testDataSource) {
            val value = it.single("""SELECT * FROM test WHERE id = 1""") { row ->
                row.toTree()
            }
            println(value)
        }
    }
}
