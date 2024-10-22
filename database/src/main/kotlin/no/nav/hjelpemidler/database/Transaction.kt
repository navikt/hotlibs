package no.nav.hjelpemidler.database

import javax.sql.DataSource

fun <T> transaction(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    block: (JdbcOperations) -> T,
): T = createSession(dataSource, returnGeneratedKeys, strict, queryTimeout).use { session ->
    session.transaction { tx -> block(SessionJdbcOperations(tx)) }
}

suspend fun <T> transactionAsync(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    block: suspend (JdbcOperations) -> T,
): T = withDatabaseContext {
    createSession(dataSource, returnGeneratedKeys, strict, queryTimeout).use { session ->
        session.transaction { tx -> block(SessionJdbcOperations(tx)) }
    }
}

interface Transaction<S : Any> {
    suspend operator fun <T> invoke(block: suspend S.() -> T): T
    suspend fun <T> transaction(block: suspend S.() -> T): T = invoke(block)
}
