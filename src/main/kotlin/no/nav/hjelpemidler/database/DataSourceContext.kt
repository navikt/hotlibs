package no.nav.hjelpemidler.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

@OptIn(DelicateCoroutinesApi::class)
internal val databaseContext: ExecutorCoroutineDispatcher =
    newSingleThreadContext("DatabaseThread")

suspend fun <T> withDatabaseContext(block: suspend CoroutineScope.() -> T): T =
    withContext(context = databaseContext, block = block)
