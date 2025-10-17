package no.nav.hjelpemidler.database

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.database.test.TestId
import no.nav.hjelpemidler.database.test.testDataSource
import no.nav.hjelpemidler.database.test.toTestEntity
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class TransactionTest {
    @Test
    fun `Lagrer og henter innslag i transaksjon`() = runTest {
        val id = transactionAsync(testDataSource) { it.lagre() }
        val entity = transactionAsync(testDataSource) { it.hent(id) }
        entity.id shouldBe id
    }

    @Test
    fun `Nestet transaksjon`() = runTest {
        val id = transactionAsync(testDataSource) { it.lagre() }
        transactionAsync(testDataSource) { tx1 ->
            someSuspendingFunction()
            val entity1 = tx1.hent(id)
            val entity2 = transactionAsync(testDataSource) { tx2 ->
                tx1 shouldBeSameInstanceAs tx2
                someSuspendingFunction()
                tx2.hent(id)
            }
            entity1 shouldBe entity2
        }
    }

    private suspend fun someSuspendingFunction() = coroutineScope {
        delay(0.1.seconds)
    }
}

private fun JdbcOperations.lagre(): TestId = single<TestId>("INSERT INTO test DEFAULT VALUES RETURNING id")
private fun JdbcOperations.hent(id: TestId) = single(
    sql = "SELECT * FROM test WHERE id = :id",
    queryParameters = id.toQueryParameters("id"),
    mapper = Row::toTestEntity,
)
