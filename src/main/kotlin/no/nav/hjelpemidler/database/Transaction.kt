package no.nav.hjelpemidler.database

import kotliquery.TransactionalSession
import java.sql.Connection

suspend inline fun <T> transaction(
    connection: Connection,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    crossinline block: suspend (TransactionalSession) -> T,
): T =
    withDatabaseContext {
        sessionOf(
            connection = connection,
            returnGeneratedKeys = returnGeneratedKeys,
            strict = strict,
            queryTimeout = queryTimeout
        ).use { session ->
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
): T =
    transaction(
        connection = factory.connection,
        returnGeneratedKeys = returnGeneratedKeys,
        strict = strict,
        queryTimeout = queryTimeout,
    ) {
        block(factory(it))
    }

suspend fun <T, X : TransactionContext> transaction(
    provider: TransactionProvider<X>,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    block: suspend (X) -> T,
): T =
    withDatabaseContext {
        val transactionContext = provider(
            sessionOf(
                connection = provider.connection,
                returnGeneratedKeys = returnGeneratedKeys,
                strict = strict,
                queryTimeout = queryTimeout
            )
        )
        provider(this) { block(transactionContext) }.await()
    }
