package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.AsyncCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture

internal class CaffeineCoroutinesCache<K : Any, V>(private val wrapped: AsyncCache<K, V>) :
    CoroutinesCache<K, V> {
    override suspend fun getIfPresent(key: K): V? = wrapped.getIfPresent(key)?.await()

    override suspend fun get(
        key: K,
        loader: suspend CoroutineScope.(K) -> V,
    ): V = coroutineScope {
        wrapped
            .get(key) { key, executor ->
                future(executor.asCoroutineDispatcher()) { loader(key) }
            }
            .await()
    }

    override suspend fun getAll(
        keys: Iterable<K>,
        loader: suspend CoroutineScope.(Set<K>) -> Map<K, V & Any>,
    ): Map<K, V & Any> = coroutineScope {
        wrapped
            .getAll(keys) { keys, executor ->
                future(executor.asCoroutineDispatcher()) { loader(keys) }
            }
            .await()
    }

    override fun put(key: K, value: V) = wrapped.put(key, CompletableFuture.completedFuture(value))

    override fun invalidate(key: K) = wrapped.synchronous().invalidate(key)
}
