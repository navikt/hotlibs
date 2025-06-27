package no.nav.hjelpemidler.database

import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import javax.sql.DataSource

class TransactionTest {
    private val dataSource = mockk<DataSource>(relaxed = true)

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
}
