package no.nav.hjelpemidler.database

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldStartWith
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.mockk
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors
import javax.sql.DataSource

class TransactionTest {
    private val dataSource = mockk<DataSource>(relaxed = true)
    private val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @Test
    fun `Nestet transaksjon bruker samme JdbcOperations`() = runTest {
        transactionAsync(dataSource) { t1 ->
            transactionAsync(dataSource) { t2 ->
                transactionAsync(dataSource) { t3 ->
                    t1 shouldBeSameInstanceAs t2
                    t2 shouldBeSameInstanceAs t3
                }
            }
        }
    }

    @Test
    fun `Feiler hvis nestet transaksjon kjører i ny tråd`() = runTest {
        transactionAsync(dataSource) {
            shouldThrow<IllegalStateException> {
                withContext(dispatcher) {
                    transactionAsync(dataSource) {
                    }
                }
            }.message.shouldStartWith("Nestet transaksjon i ny tråd")
        }
    }
}
