package no.nav.hjelpemidler.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

@OptIn(DelicateCoroutinesApi::class)
val DataSourceContext = newSingleThreadContext("DataSourceContext")

suspend fun <T> withDataSourceContext(block: suspend CoroutineScope.() -> T): T =
    withContext(context = DataSourceContext, block = block)
