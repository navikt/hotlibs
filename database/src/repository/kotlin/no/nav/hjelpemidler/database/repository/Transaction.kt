package no.nav.hjelpemidler.database.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.database.hibernate.HibernateTransactionContext
import no.nav.hjelpemidler.database.hibernate.StatelessSessionRepositoryOperations
import no.nav.hjelpemidler.database.hibernate.manageTransaction
import org.hibernate.SessionFactory

suspend fun <T> transactionAsync(
    factory: SessionFactory,
    block: suspend (RepositoryOperations) -> T,
): T {
    val context = currentCoroutineContext()[HibernateTransactionContext] ?: return withContext(Dispatchers.IO) {
        factory.openStatelessSession().use { session ->
            val transaction = session.beginTransaction()
            manageTransaction(session, transaction) {
                val tx = StatelessSessionRepositoryOperations(session)
                withContext(HibernateTransactionContext(tx, transaction, Thread.currentThread())) {
                    block(tx)
                }
            }
        }
    }
    val currentThread = Thread.currentThread()
    check(currentThread == context.thread) {
        "Nestet transaksjon i ny tråd: $currentThread != ${context.thread}"
    }
    return block(context.tx)
}
