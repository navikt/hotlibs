package no.nav.hjelpemidler.database

import kotlinx.coroutines.currentCoroutineContext
import no.nav.hjelpemidler.database.kotliquery.SessionProperties
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

internal class JdbcTransactionContext(
    val properties: SessionProperties,
    val tx: JdbcOperations,
) : AbstractCoroutineContextElement(JdbcTransactionContext) {
    companion object Key : CoroutineContext.Key<JdbcTransactionContext>
}

internal suspend fun currentTransactionContext(): JdbcTransactionContext? =
    currentCoroutineContext()[JdbcTransactionContext]
