package no.nav.hjelpemidler.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import javax.sql.DataSource

interface Transaction<S : Any> {
    suspend operator fun <T> invoke(block: suspend S.() -> T): T
}

fun <T> transaction(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    block: (JdbcOperations) -> T,
): T = createSession(dataSource, returnGeneratedKeys, strict, queryTimeout).use { session ->
    session.transaction { block(SessionJdbcOperations(it)) }
}

suspend fun <T> transactionAsync(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    block: suspend (JdbcOperations) -> T,
): T {
    val context = currentCoroutineContext()[TransactionContext] ?: return withDatabaseContext {
        createSession(dataSource, returnGeneratedKeys, strict, queryTimeout).use { session ->
            session.transaction {
                val tx = SessionJdbcOperations(it)
                withContext(Dispatchers.Transaction) {
                    withContext(TransactionContext(tx, Thread.currentThread().name)) {
                        block(tx)
                    }
                }
            }
        }
    }
    val currentThreadName = Thread.currentThread().name
    check(currentThreadName == context.threadName) {
        "Nestet transaksjon i ny tråd: '$currentThreadName' != '${context.threadName}'"
    }
    return block(context.tx)
}
