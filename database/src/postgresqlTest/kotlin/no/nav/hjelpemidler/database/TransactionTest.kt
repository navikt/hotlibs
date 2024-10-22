package no.nav.hjelpemidler.database

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.database.test.TestEntity
import no.nav.hjelpemidler.database.test.testDatabase
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class TransactionTest {
    @Test
    fun `Lagrer og henter innslag i transaksjon`() = runTest {
        val id = testDatabase { lagre(TestEntity()) }
        val entity = testDatabase { hent(id) }
        entity.id shouldBe id
    }

    @Test
    fun `Nestet transaksjon`() = runTest {
        val id = testDatabase { lagre(TestEntity()) }
        testDatabase {
            someSuspendingFunction()
            val entity1 = hent(id)
            val entity2 = testDatabase {
                someSuspendingFunction()
                hent(id)
            }
            entity1 shouldBe entity2
        }
    }

    private suspend fun someSuspendingFunction() =
        withContext(Dispatchers.IO) {
            delay(0.1.seconds)
        }
}
