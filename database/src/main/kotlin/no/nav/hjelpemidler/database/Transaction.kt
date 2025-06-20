package no.nav.hjelpemidler.database

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import javax.sql.DataSource
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

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
    val context = currentCoroutineContext()[JdbcTransactionContext] ?: return withContext(Dispatchers.IO) {
        createSession(dataSource, returnGeneratedKeys, strict, queryTimeout).use { session ->
            withContext(transactionCoroutineDispatcher()) {
                session.transaction {
                    val tx = SessionJdbcOperations(it)
                    withContext(JdbcTransactionContext(tx, Thread.currentThread())) {
                        block(tx)
                    }
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

fun transactionCoroutineDispatcher(): CoroutineDispatcher =
    Executors.newSingleThreadExecutor().asCoroutineDispatcher()

private class JdbcTransactionContext(val tx: JdbcOperations, val thread: Thread) :
    AbstractCoroutineContextElement(JdbcTransactionContext) {
    companion object Key : CoroutineContext.Key<JdbcTransactionContext>
}
