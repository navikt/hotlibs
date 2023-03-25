package no.nav.hjelpemidler.database

import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.database.test.Test1Entity
import no.nav.hjelpemidler.database.test.TestEnum
import no.nav.hjelpemidler.database.test.TestStoreContext
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class TransactionTest {
    private val storeContext = TestStoreContext()

    @Test
    fun `lagrer og henter innslag med transaction context`() = runTest {
        val id = lagreEntity()
        val result = transaction(storeContext) { ctx ->
            ctx.testStore.hent(id)
        }
        result.shouldContain("id", id)
    }

    @Test
    fun `nested transaction`() = runTest {
        val id = lagreEntity()
        val result = transaction(storeContext) { ctx1 ->
            someSuspendingFunction()
            ctx1.testStore.hent(id)
            transaction(storeContext) { ctx2 ->
                someSuspendingFunction()
                ctx2.testStore.hent(id)
            }
        }
        result["id"] shouldBe id
    }

    private suspend fun lagreEntity(): Long =
        transaction(storeContext) { ctx ->
            ctx.testStore.lagre(
                Test1Entity(
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
