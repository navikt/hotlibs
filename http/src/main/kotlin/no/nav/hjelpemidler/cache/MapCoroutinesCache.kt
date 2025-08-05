package no.nav.hjelpemidler.cache

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.ConcurrentHashMap

/**
 * Implementasjon av [CoroutinesCache] basert på [ConcurrentHashMap] uten eviction.
 */
internal class MapCoroutinesCache<K : Any, V> private constructor(
    private val wrapped: MutableMap<K, V>,
) : CoroutinesCache<K, V> {
    constructor() : this(ConcurrentHashMap())

    override suspend fun getIfPresent(key: K): V? =
        wrapped[key]

    override suspend fun get(key: K, loader: suspend CoroutineScope.(K) -> V): V = coroutineScope {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(
        keys: Iterable<K>,
        loader: suspend CoroutineScope.(Set<K>) -> Map<K, V & Any>,
    ): Map<K, V & Any> = coroutineScope {
        TODO("Not yet implemented")
    }

    override suspend fun put(key: K, value: V) {
        wrapped[key] = value
    }

    override suspend fun computeIfAbsent(key: K, loader: suspend CoroutineScope.(K) -> V): V? = coroutineScope {
        TODO("Not yet implemented")
    }

    override suspend fun computeIfPresent(key: K, loader: suspend CoroutineScope.(K, V) -> V): V? = coroutineScope {
        TODO("Not yet implemented")
    }

    override suspend fun compute(key: K, loader: suspend CoroutineScope.(K, V?) -> V): V? = coroutineScope {
        TODO("Not yet implemented")
    }

    override suspend fun remove(key: K): V? =
        wrapped.remove(key)
}
