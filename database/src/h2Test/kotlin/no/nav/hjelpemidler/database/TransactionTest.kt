package no.nav.hjelpemidler.database

import io.kotest.matchers.types.shouldBeSameInstanceAs
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TransactionTest {
    private val dataSource = createDataSource(H2)

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
