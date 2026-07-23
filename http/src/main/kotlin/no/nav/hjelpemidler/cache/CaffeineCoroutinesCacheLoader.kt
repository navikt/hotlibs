package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.AsyncCacheLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

internal class CaffeineCoroutinesCacheLoader<K : Any, V>(
    private val scope: CoroutineScope,
    private val loader: CoroutinesCacheLoader<K, V>,
) : AsyncCacheLoader<K, V> {
    override fun asyncLoad(key: K, executor: Executor): CompletableFuture<V> =
        future(executor) { loader.load(key) }

    override fun asyncLoadAll(keys: Set<K>, executor: Executor): CompletableFuture<Map<K, V & Any>> =
        future(executor) { loader.loadAll(keys) }

    override fun asyncReload(key: K, oldValue: V & Any, executor: Executor): CompletableFuture<V> =
        future(executor) { loader.reload(key, oldValue) }

    private fun <T> future(executor: Executor, block: suspend CoroutineScope.() -> T): CompletableFuture<T> =
        scope.future(executor.asCoroutineDispatcher()) { block() }
}
