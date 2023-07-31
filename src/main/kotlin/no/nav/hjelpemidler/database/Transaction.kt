package no.nav.hjelpemidler.database

import kotliquery.TransactionalSession
import kotliquery.sessionOf
import javax.sql.DataSource

suspend inline fun <T> transaction(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    crossinline block: suspend (TransactionalSession) -> T,
): T = withDatabaseContext {
    sessionOf(dataSource, returnGeneratedKeys, strict, queryTimeout).use { session ->
        session.transaction {
            block(it)
        }
    }
}

suspend inline fun <T, X : TransactionContext> transaction(
    factory: TransactionContextFactory<X>,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    crossinline block: suspend (X) -> T,
): T = transaction(factory.dataSource, returnGeneratedKeys, strict, queryTimeout) {
    block(factory(it))
}
