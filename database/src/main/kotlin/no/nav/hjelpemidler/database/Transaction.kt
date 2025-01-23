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
    val context = currentCoroutineContext()[TransactionContext] ?: return withContext(Dispatchers.IO) {
        createSession(dataSource, returnGeneratedKeys, strict, queryTimeout).use { session ->
            withContext(TransactionCoroutineDispatcher) {
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

private val TransactionCoroutineDispatcher: CoroutineDispatcher =
    Executors.newSingleThreadExecutor().asCoroutineDispatcher()

private class TransactionContext(val tx: JdbcOperations, val threadId: Long) :
    AbstractCoroutineContextElement(TransactionContext) {
    companion object Key : CoroutineContext.Key<TransactionContext>
}
