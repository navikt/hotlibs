package no.nav.hjelpemidler.cache

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.ConcurrentHashMap

/**
 * Implementasjon av [CoroutinesCache] basert på [ConcurrentHashMap] uten eviction.
 */
class MapCoroutinesCache<K : Any, V> private constructor(
    private val wrapped: MutableMap<K, Deferred<V>>,
) : CoroutinesCache<K, V> {
    constructor() : this(ConcurrentHashMap())

    override suspend fun getIfPresent(key: K): V? = wrapped[key]?.await()

    override suspend fun get(key: K, loader: suspend CoroutineScope.(K) -> V): V = computeIfAbsent(key, loader)

    override suspend fun getAll(
        keys: Iterable<K>,
        loader: suspend CoroutineScope.(Set<K>) -> Map<K, V & Any>,
    ): Map<K, V & Any> = coroutineScope {
        val result = mutableMapOf<K, V & Any>()
        val missingKeys = mutableSetOf<K>()

        keys.forEach { key ->
            getIfPresent(key)?.let { value ->
                result[key] = value
            } ?: run {
                missingKeys.add(key)
            }
        }

        if (missingKeys.isNotEmpty()) {
            val value = loader(missingKeys)
            value.forEach { (key, value) ->
                result[key] = value
                put(key, value)
            }
        }

        result
    }

    override suspend fun put(key: K, value: V) {
        wrapped[key] = CompletableDeferred(value)
    }

    override suspend fun computeIfAbsent(key: K, loader: suspend CoroutineScope.(K) -> V): V = coroutineScope {
        wrapped
            .computeIfAbsent(key) { key -> async { loader(key) } }
            .await()
    }

    override suspend fun computeIfPresent(key: K, loader: suspend CoroutineScope.(K, V) -> V): V? = coroutineScope {
        wrapped
            .computeIfPresent(key) { key, value -> async { loader(key, value.await()) } }
            ?.await()
    }

    override suspend fun compute(key: K, loader: suspend CoroutineScope.(K, V?) -> V): V? = coroutineScope {
        wrapped
            .compute(key) { key, value -> async { loader(key, value?.await()) } }
            ?.await()
    }

    override suspend fun remove(key: K): V? = wrapped.remove(key)?.await()

    override suspend fun asMap(): Map<K, Deferred<V>> = wrapped.toMap()
}
