package no.nav.hjelpemidler.database

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
            withTransactionContext {
                session.transaction {
                    val tx = SessionJdbcOperations(it)
                    withContext(TransactionContext(tx, Thread.currentThread().threadId())) {
                        block(tx)
                    }
                }
            }
        }
    }
    val currentThreadId = Thread.currentThread().threadId()
    check(currentThreadId == context.threadId) {
        "Nestet transaksjon i ny tråd: $currentThreadId != ${context.threadId}"
    }
    return block(context.tx)
}
