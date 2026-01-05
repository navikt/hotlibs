package no.nav.hjelpemidler.database

import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.database.test.migrate
import no.nav.hjelpemidler.database.test.testDataSource
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import java.time.Instant
import java.time.ZonedDateTime
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
                row.asTree()
            }
            println(value)
            val r1 = it.single<Instant>("SELECT timestamp_with_timezone FROM test WHERE id = 1")
            println(r1)
            val r2 = it.single<ZonedDateTime>("SELECT timestamp_with_timezone FROM test WHERE id = 1")
            println(r2)
        }
    }
}
