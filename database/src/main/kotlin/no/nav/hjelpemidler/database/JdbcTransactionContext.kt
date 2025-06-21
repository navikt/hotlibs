package no.nav.hjelpemidler.database

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

internal class JdbcTransactionContext(val tx: JdbcOperations, val thread: Thread) :
    AbstractCoroutineContextElement(JdbcTransactionContext) {
    companion object Key : CoroutineContext.Key<JdbcTransactionContext>
}
