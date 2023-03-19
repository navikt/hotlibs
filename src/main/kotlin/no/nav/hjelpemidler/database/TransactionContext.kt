package no.nav.hjelpemidler.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotliquery.TransactionalSession
import javax.sql.DataSource
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

interface TransactionContext

class TransactionContextElement(internal val tx: TransactionalSession) :
    AbstractCoroutineContextElement(TransactionContextElement) {
    companion object Key : CoroutineContext.Key<TransactionContextElement>
}

fun CoroutineContext.transactionContext(): TransactionContextElement =
    checkNotNull(this[TransactionContextElement.Key]) {
        "TransactionContextElement mangler!"
    }

fun CoroutineContext.tx(): TransactionalSession =
    transactionContext().tx

suspend fun <T> withTransactionContext(
    dataSource: DataSource,
    block: suspend CoroutineScope.() -> T,
): T {
    TODO("Ikke ferdig!")
}

suspend fun <T> withTransactionContext(
    tx: TransactionalSession,
    block: suspend CoroutineScope.() -> T,
): T {
    return withContext(TransactionContextElement(tx), block)
}
