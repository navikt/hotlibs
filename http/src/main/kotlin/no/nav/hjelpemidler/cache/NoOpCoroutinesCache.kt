package no.nav.hjelpemidler.cache

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

/**
 * Implementasjon av [CoroutinesCache] som ikke cacher.
 */
class NoOpCoroutinesCache<K : Any, V> : CoroutinesCache<K, V> {
    override suspend fun getIfPresent(key: K): V? =
        null

    override suspend fun get(key: K, loader: suspend CoroutineScope.(K) -> V): V = coroutineScope {
        loader(key)
    }

    override suspend fun getAll(
        keys: Iterable<K>,
        loader: suspend CoroutineScope.(Set<K>) -> Map<K, V & Any>,
    ): Map<K, V & Any> = coroutineScope {
        loader(keys.toSet())
    }

    override suspend fun put(key: K, value: V) =
        Unit

    override suspend fun computeIfAbsent(key: K, loader: suspend CoroutineScope.(K) -> V): V? = coroutineScope {
        loader(key)
    }

    override suspend fun computeIfPresent(key: K, loader: suspend CoroutineScope.(K, V) -> V): V? =
        null

    override suspend fun compute(key: K, loader: suspend CoroutineScope.(K, V?) -> V): V? = coroutineScope {
        loader(key, null)
    }

    override suspend fun remove(key: K): V? =
        null
}
