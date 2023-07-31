package no.nav.hjelpemidler.database.test

import kotliquery.Session
import no.nav.hjelpemidler.database.TransactionContextFactory
import javax.sql.DataSource

class TestTransactionContextFactory : TransactionContextFactory<TestTransactionContext> {
    override val dataSource: DataSource = testDataSource

    override operator fun invoke(session: Session): TestTransactionContext =
        object : TestTransactionContext {
            override val testStore: TestStore = TestStore(session)
        }
}
