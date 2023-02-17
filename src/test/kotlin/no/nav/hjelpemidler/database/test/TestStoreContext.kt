package no.nav.hjelpemidler.database.test

import kotliquery.TransactionalSession
import no.nav.hjelpemidler.database.Store
import no.nav.hjelpemidler.database.StoreContext
import no.nav.hjelpemidler.database.TransactionContext
import no.nav.hjelpemidler.database.single
import no.nav.hjelpemidler.database.toMap
import javax.sql.DataSource

class TestStore(private val tx: TransactionalSession) : Store {
    fun hent(id: Long): Map<String, Any?> = tx.single(
        sql = """
            SELECT *
            FROM test_1
            WHERE id = :id
        """.trimIndent(),
        queryParameters = mapOf("id" to id)
    ) {
        it.toMap()
    }
}

interface TestTransactionContext : TransactionContext {
    val testStore: TestStore
}

class TestStoreContext(override val dataSource: DataSource) : StoreContext<TestTransactionContext> {
    override fun transactionContext(tx: TransactionalSession): TestTransactionContext =
        object : TestTransactionContext {
            override val testStore: TestStore = TestStore(tx)
        }
}
