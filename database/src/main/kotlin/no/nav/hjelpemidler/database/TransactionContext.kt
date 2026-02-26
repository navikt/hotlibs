package no.nav.hjelpemidler.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal suspend fun <S : AutoCloseable, T> withTransactionContext(
    closeable: S,
    block: suspend CoroutineScope.(S) -> T,
): T = withContext(Dispatchers.IO) { closeable.use { block(it) } }
