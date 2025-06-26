package no.nav.hjelpemidler.database.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.database.hibernate.HibernateTransactionContext
import no.nav.hjelpemidler.database.hibernate.StatelessSessionRepositoryOperations
import no.nav.hjelpemidler.database.hibernate.statelessTransaction
import org.hibernate.SessionFactory

suspend fun <T> transactionAsync(
    factory: SessionFactory,
    block: suspend (RepositoryOperations) -> T,
): T {
    val context = currentCoroutineContext()[HibernateTransactionContext] ?: return withContext(Dispatchers.IO) {
        factory.statelessTransaction { session ->
            val tx = StatelessSessionRepositoryOperations(session)
            withContext(HibernateTransactionContext(tx, Thread.currentThread())) {
                block(tx)
            }
        }
    }
    /*
    val currentThread = Thread.currentThread()
    check(currentThread == context.thread) {
        "Nestet transaksjon i ny tråd: $currentThread != ${context.thread}"
    }
    */
    return block(context.tx)
}
