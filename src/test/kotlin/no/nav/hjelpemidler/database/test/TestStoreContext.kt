package no.nav.hjelpemidler.database.test

import kotliquery.TransactionalSession
import no.nav.hjelpemidler.database.StoreContext
import no.nav.hjelpemidler.database.createDataSource
import no.nav.hjelpemidler.database.migrate
import javax.sql.DataSource

val testDataSource: DataSource =
    createDataSource {
        testcontainers("15-alpine")
    }.also {
        it.migrate()
    }

interface TestTransactionContext {
    val testStore: TestStore
}

class TestStoreContext : StoreContext<TestTransactionContext> {
    override val dataSource: DataSource = testDataSource

    override fun createTransactionContext(tx: TransactionalSession): TestTransactionContext =
        object : TestTransactionContext {
            override val testStore: TestStore = TestStore(tx)
        }
}
