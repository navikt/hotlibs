package no.nav.hjelpemidler.database

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

internal class JdbcTransactionContext(val tx: JdbcOperations) :
    AbstractCoroutineContextElement(JdbcTransactionContext) {
    companion object Key : CoroutineContext.Key<JdbcTransactionContext>
}
