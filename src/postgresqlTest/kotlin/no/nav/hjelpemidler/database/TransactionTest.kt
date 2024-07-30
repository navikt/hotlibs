package no.nav.hjelpemidler.database

import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.database.test.TestEntity
import no.nav.hjelpemidler.database.test.TestEnum
import no.nav.hjelpemidler.database.test.TestId
import no.nav.hjelpemidler.database.test.TestStore
import no.nav.hjelpemidler.database.test.testDataSource
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class TransactionTest {
    @Test
    fun `Lagrer og henter innslag i transaksjon`() = runTest {
        val id = lagreEntity()
        val result = transactionAsync(testDataSource) { tx ->
            TestStore(tx).hent(id)
        }
        result.shouldContain("id", id.queryParameter)
    }

    @Test
    fun `Nestet transaksjon`() = runTest {
        val id = lagreEntity()
        val result = transactionAsync(testDataSource) { tx1 ->
            someSuspendingFunction()
            TestStore(tx1).hent(id)
            transactionAsync(testDataSource) { tx2 ->
                someSuspendingFunction()
                TestStore(tx2).hent(id)
            }
        }
        result["id"] shouldBe id.queryParameter
    }

    private suspend fun lagreEntity(): TestId = transactionAsync(testDataSource) { tx ->
        TestStore(tx).lagre(
            TestEntity(
                string = "string",
                integer = 1,
                enum = TestEnum.A,
                data1 = emptyMap(),
            )
        )
    }

    private suspend fun someSuspendingFunction() =
        withContext(Dispatchers.IO) {
            delay(0.1.seconds)
        }
}
