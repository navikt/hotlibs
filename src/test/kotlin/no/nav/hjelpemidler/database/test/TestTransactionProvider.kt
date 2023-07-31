package no.nav.hjelpemidler.database.test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotliquery.Session
import no.nav.hjelpemidler.database.TransactionProvider
import java.sql.Connection

class TestTransactionProvider : TransactionProvider<TestTransactionContext> {
    override val connection: Connection
        get() = testDataSource.connection

    override operator fun invoke(session: Session): TestTransactionContext =
        object : TestTransactionContext {
            override val testStore: TestStore = TestStore(session)
        }

    override fun <T> invoke(coroutineScope: CoroutineScope, block: suspend () -> T): Deferred<T> =
        coroutineScope.async { block() }
}
