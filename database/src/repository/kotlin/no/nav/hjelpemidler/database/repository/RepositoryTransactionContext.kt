package no.nav.hjelpemidler.database.repository

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

internal class RepositoryTransactionContext(val tx: RepositoryOperations) :
    AbstractCoroutineContextElement(RepositoryTransactionContext) {
    companion object Key : CoroutineContext.Key<RepositoryTransactionContext>
}
