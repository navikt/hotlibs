package no.nav.hjelpemidler.database.test

import no.nav.hjelpemidler.database.Transaction
import no.nav.hjelpemidler.database.migrate
import no.nav.hjelpemidler.database.transactionAsync
import javax.sql.DataSource

class TestDatabase(private val dataSource: DataSource) : Transaction<TestStore> {
    fun migrate() = dataSource.migrate()

    override suspend fun <T> invoke(block: suspend TestStore.() -> T): T = transactionAsync(dataSource) { tx ->
        block(TestStore(tx))
    }
}

val testDatabase by lazy { TestDatabase(testDataSource).also(TestDatabase::migrate) }
