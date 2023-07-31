package no.nav.hjelpemidler.database

import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.database.test.TestEntity
import no.nav.hjelpemidler.database.test.TestEnum
import no.nav.hjelpemidler.database.test.TestTransactionContextFactory
import no.nav.hjelpemidler.database.test.TestTransactionProvider
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class TransactionTest {
    private val transactionContextFactory = TestTransactionContextFactory()
    private val transactionProvider = TestTransactionProvider()

    @Test
    fun `lagrer og henter innslag i transaksjon`() = runTest {
        val id = lagreEntity()
        val result = transaction(transactionContextFactory) { ctx ->
            ctx.testStore.hent(id)
        }
        result.shouldContain("id", id)
    }

    @Test
    fun `nestet transaksjon`() = runTest {
        val id = lagreEntity()
        val result = transaction(transactionContextFactory) { ctx1 ->
            someSuspendingFunction()
            ctx1.testStore.hent(id)
            transaction(transactionContextFactory) { ctx2 ->
                someSuspendingFunction()
                ctx2.testStore.hent(id)
            }
        }
        result["id"] shouldBe id
    }

    @Test
    fun `lagrer og henter innslag med transaction provider`() = runTest {
        val id = lagreEntity()
        val result = transaction(transactionProvider) { ctx ->
            ctx.testStore.hent(id)
        }
        result.shouldContain("id", id)
    }

    private suspend fun lagreEntity(): Long = transaction(transactionContextFactory) { ctx ->
            ctx.testStore.lagre(
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
