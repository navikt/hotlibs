package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.AsyncCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture

/**
 * NB! Bruker [CompletableFuture.asDeferred] for å unngå at kansellering propagerer til flere kallere.
 */
internal class CaffeineCoroutinesCache<K : Any, V>(
    private val scope: CoroutineScope,
    private val wrapped: AsyncCache<K, V>,
) : CoroutinesCache<K, V> {
    override suspend fun get(key: K): V? = wrapped
        .getIfPresent(key)
        ?.asDeferred()
        ?.await()

    override suspend fun getOrLoad(
        key: K,
        block: suspend CoroutineScope.(K) -> V,
    ): V = wrapped
        .get(key) { key, _ -> scope.future { block(key) } }
        .asDeferred()
        .await()

    override suspend fun getOrLoadAll(
        keys: Iterable<K>,
        block: suspend CoroutineScope.(Set<K>) -> Map<K, V & Any>,
    ): Map<K, V & Any> = wrapped
        .getAll(keys) { keys, _ -> scope.future { block(keys) } }
        .asDeferred()
        .await()

    override suspend fun put(key: K, value: V): Unit = wrapped.put(key, CompletableFuture.completedFuture(value))

    override suspend fun putAll(from: Map<K, V & Any>): Unit = wrapped.synchronous().putAll(from)

    override suspend fun invalidate(key: K): Unit = wrapped.synchronous().invalidate(key)

    override suspend fun invalidateAll(keys: Iterable<K>): Unit = wrapped.synchronous().invalidateAll(keys)
}
