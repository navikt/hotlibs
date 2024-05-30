package no.nav.hjelpemidler.database

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import org.jetbrains.annotations.BlockingExecutor
import java.util.concurrent.Executors

internal val VirtualCoroutineDispatcher: ExecutorCoroutineDispatcher =
    Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()

@Suppress("UnusedReceiverParameter")
val Dispatchers.Virtual: @BlockingExecutor CoroutineDispatcher
    get() = VirtualCoroutineDispatcher
