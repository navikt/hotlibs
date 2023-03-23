package no.nav.hjelpemidler.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.database.test.AbstractDatabaseTest
import no.nav.hjelpemidler.database.test.shouldBe
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class TransactionTest : AbstractDatabaseTest() {
    @Test
    fun `henter innslag med transaction context`() = runTest {
        val result = transaction(storeContext) { ctx ->
            ctx.testStore.hent(1)
        }
        result["id"] shouldBe 1L
    }

    @Test
    fun `nested transaction`() = runTest {
        val result = transaction(storeContext) { ctx1 ->
            someSuspendingFunction()
            ctx1.testStore.hent(1)
            transaction(storeContext) { ctx2 ->
                someSuspendingFunction()
                ctx2.testStore.hent(1)
            }
        }
        result["id"] shouldBe 1L
    }

    private suspend fun someSuspendingFunction() = withContext(Dispatchers.IO) {
        delay(0.1.seconds)
    }
}
