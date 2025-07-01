package no.nav.hjelpemidler.database.repository

import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.database.hibernate.StatelessSessionRepositoryOperations
import no.nav.hjelpemidler.database.hibernate.statelessTransaction
import org.hibernate.SessionFactory

suspend fun <T> transactionAsync(
    factory: SessionFactory,
    block: suspend (RepositoryOperations) -> T,
): T {
    val context = currentTransactionContext() ?: return factory.statelessTransaction { session ->
        val tx = StatelessSessionRepositoryOperations(session)
        withContext(RepositoryTransactionContext(tx)) {
            block(tx)
        }
    }
    return block(context.tx)
}
