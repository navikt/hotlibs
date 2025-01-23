package no.nav.hjelpemidler.database

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

internal class TransactionContext(val tx: JdbcOperations, val threadId: Long) :
    AbstractCoroutineContextElement(TransactionContext) {
    companion object Key : CoroutineContext.Key<TransactionContext>
}
