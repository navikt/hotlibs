package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.database.repository.RepositoryOperations
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

internal class HibernateTransactionContext(
    val tx: RepositoryOperations,
    val thread: Thread,
) : AbstractCoroutineContextElement(HibernateTransactionContext) {
    companion object Key : CoroutineContext.Key<HibernateTransactionContext>
}
