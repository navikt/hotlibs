package no.nav.hjelpemidler.database

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal val DatabaseDispatcher: CoroutineDispatcher =
    Dispatchers.IO.limitedParallelism(parallelism = HIKARI_MAXIMUM_POOL_SIZE, name = "DatabaseDispatcher")

suspend fun <T> withDatabaseContext(block: suspend CoroutineScope.() -> T): T =
    withContext(DatabaseDispatcher, block)
