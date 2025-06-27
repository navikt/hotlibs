package no.nav.hjelpemidler.database.repository

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.database.hibernate.StatelessSessionRepositoryOperations
import no.nav.hjelpemidler.database.hibernate.statelessTransaction
import no.nav.hjelpemidler.database.withDatabaseContext
import org.hibernate.SessionFactory

suspend fun <T> transactionAsync(
    factory: SessionFactory,
    block: suspend (RepositoryOperations) -> T,
): T {
    val context = currentCoroutineContext()[RepositoryTransactionContext] ?: return withDatabaseContext {
        factory.statelessTransaction { session ->
            val tx = StatelessSessionRepositoryOperations(session)
            withContext(RepositoryTransactionContext(tx)) {
                block(tx)
            }
        }
    }
    return block(context.tx)
}
