package no.nav.hjelpemidler.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred

interface TransactionProvider<X : TransactionContext> : TransactionContextFactory<X> {
    operator fun <T> invoke(coroutineScope: CoroutineScope, block: suspend () -> T): Deferred<T>
}
