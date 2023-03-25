package no.nav.hjelpemidler.database

import kotliquery.TransactionalSession
import kotliquery.sessionOf
import javax.sql.DataSource

suspend inline fun <T> transaction(
    dataSource: DataSource,
    returnGeneratedKey: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    crossinline block: suspend (TransactionalSession) -> T,
): T =
    withDatabaseContext {
        sessionOf(
            dataSource = dataSource,
            returnGeneratedKey = returnGeneratedKey,
            strict = strict,
            queryTimeout = queryTimeout
        ).use { session ->
            session.transaction { tx ->
                block(tx)
            }
        }
    }

suspend inline fun <T, X : Any> transaction(
    storeContext: StoreContext<X>,
    returnGeneratedKey: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    crossinline block: suspend (X) -> T,
): T =
    transaction(
        dataSource = storeContext.dataSource,
        returnGeneratedKey = returnGeneratedKey,
        strict = strict,
        queryTimeout = queryTimeout,
    ) { tx ->
        block(storeContext(tx))
    }
