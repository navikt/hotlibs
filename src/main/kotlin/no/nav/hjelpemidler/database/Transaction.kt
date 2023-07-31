package no.nav.hjelpemidler.database

import kotliquery.TransactionalSession
import javax.sql.DataSource


suspend inline fun <T> transaction(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    crossinline block: suspend (TransactionalSession) -> T,
): T =
    withDatabaseContext {
        sessionOf(
            connection = dataSource.connection,
            returnGeneratedKeys = returnGeneratedKeys,
            strict = strict,
            queryTimeout = queryTimeout
        ).use { session ->
            session.transaction {
                block(it)
            }
        }
    }

suspend inline fun <T, X : Any> transaction(
    storeContext: StoreContext<X>,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    crossinline block: suspend (X) -> T,
): T =
    transaction(
        dataSource = storeContext.dataSource,
        returnGeneratedKeys = returnGeneratedKeys,
        strict = strict,
        queryTimeout = queryTimeout,
    ) {
        block(storeContext(it))
    }

suspend inline fun <T, X : Any> transaction(
    storeContext: TransactionStoreContext<X>,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    crossinline block: suspend (X) -> T,
): T =
    withDatabaseContext {
        val transactionContext = storeContext(
            sessionOf(
                connection = storeContext.connection,
                returnGeneratedKeys = returnGeneratedKeys,
                strict = strict,
                queryTimeout = queryTimeout
            )
        )
        storeContext.execute { block(transactionContext) }
    }
