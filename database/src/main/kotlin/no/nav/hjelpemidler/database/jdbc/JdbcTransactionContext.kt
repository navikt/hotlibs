package no.nav.hjelpemidler.database.jdbc

import kotlinx.coroutines.currentCoroutineContext
import no.nav.hjelpemidler.database.JdbcOperations
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

internal class JdbcTransactionContext(val tx: JdbcOperations) :
    AbstractCoroutineContextElement(JdbcTransactionContext) {
    companion object Key : CoroutineContext.Key<JdbcTransactionContext>
}

internal suspend fun currentTransactionContext(): JdbcTransactionContext? =
    currentCoroutineContext()[JdbcTransactionContext]
