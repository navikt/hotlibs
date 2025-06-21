package no.nav.hjelpemidler.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import javax.sql.DataSource

interface Transaction<T : Any> {
    suspend operator fun <R> invoke(block: suspend T.() -> R): R
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
    val context = currentCoroutineContext()[JdbcTransactionContext] ?: return withContext(Dispatchers.IO) {
        createSession(dataSource, returnGeneratedKeys, strict, queryTimeout).use { session ->
            session.transaction {
                val tx = SessionJdbcOperations(it)
                withContext(JdbcTransactionContext(tx, Thread.currentThread())) {
                    block(tx)
                }
            }
        }
    }
    val currentThread = Thread.currentThread()
    check(currentThread == context.thread) {
        "Nestet transaksjon i ny tråd: $currentThread != ${context.thread}"
    }
    return block(context.tx)
}
