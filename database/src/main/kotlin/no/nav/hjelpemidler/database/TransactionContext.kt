package no.nav.hjelpemidler.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> withTransactionContext(block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.IO, block)

suspend fun <S : AutoCloseable, T> withTransactionContext(closeable: S, block: suspend CoroutineScope.(S) -> T): T =
    withTransactionContext { closeable.use { block(it) } }
