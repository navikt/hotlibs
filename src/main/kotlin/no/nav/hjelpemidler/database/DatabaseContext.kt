package no.nav.hjelpemidler.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
val DatabaseContext: ExecutorCoroutineDispatcher =
    newSingleThreadContext("DatabaseThread")

suspend fun <T> withDatabaseContext(block: suspend CoroutineScope.() -> T): T =
    withContext(context = DatabaseContext, block = block)
