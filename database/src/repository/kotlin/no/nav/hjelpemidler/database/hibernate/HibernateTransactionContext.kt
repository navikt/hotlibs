package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.database.repository.RepositoryOperations
import org.hibernate.Transaction
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

internal class HibernateTransactionContext(
    val tx: RepositoryOperations,
    val transaction: Transaction,
    val thread: Thread,
) : AbstractCoroutineContextElement(HibernateTransactionContext) {
    companion object Key : CoroutineContext.Key<HibernateTransactionContext>
}
