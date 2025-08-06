package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.AsyncCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture

internal class CaffeineCoroutinesCache<K : Any, V>(private val wrapped: AsyncCache<K, V>) : CoroutinesCache<K, V> {
    override suspend fun getIfPresent(key: K): V? = coroutineScope {
        wrapped.getIfPresent(key)?.await()
    }

    override suspend fun get(key: K, loader: suspend CoroutineScope.(K) -> V): V = coroutineScope {
        wrapped.get(key) { key, _ ->
            future { loader(key) }
        }.await()
    }

    override suspend fun getAll(
        keys: Iterable<K>,
        loader: suspend CoroutineScope.(Set<K>) -> Map<K, V & Any>,
    ): Map<K, V & Any> = coroutineScope {
        wrapped.getAll(keys) { keys, _ ->
            future { loader(keys) }
        }.await()
    }

    override suspend fun put(key: K, value: V) = coroutineScope {
        wrapped.put(key, CompletableFuture.completedFuture(value))
    }

    override suspend fun computeIfAbsent(key: K, loader: suspend CoroutineScope.(K) -> V): V = coroutineScope {
        wrapped.asMap().computeIfAbsent(key) { key ->
            future { loader(key) }
        }.await()
    }

    override suspend fun computeIfPresent(key: K, loader: suspend CoroutineScope.(K, V) -> V): V? = coroutineScope {
        wrapped.asMap().computeIfPresent(key) { key, value ->
            future { loader(key, value.await()) }
        }?.await()
    }

    override suspend fun compute(key: K, loader: suspend CoroutineScope.(K, V?) -> V): V? = coroutineScope {
        wrapped.asMap().compute(key) { key, value ->
            future { loader(key, value?.await()) }
        }?.await()
    }

    override suspend fun remove(key: K): V? = coroutineScope {
        wrapped.asMap().remove(key)?.await()
    }

    override suspend fun asMap(): Map<K, Deferred<V>> =
        wrapped.asMap().mapValues { (_, value) -> value.asDeferred() }
}
