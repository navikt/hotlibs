package no.nav.hjelpemidler.cache

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

class NoOpCoroutinesCache<K : Any, V> : CoroutinesCache<K, V> {
    override suspend fun get(key: K): V? = null

    override suspend fun getOrLoad(key: K, block: suspend CoroutineScope.(K) -> V): V = coroutineScope { block(key) }

    override suspend fun getOrLoadAll(
        keys: Iterable<K>,
        block: suspend CoroutineScope.(Set<K>) -> Map<K, V & Any>,
    ): Map<K, V & Any> = coroutineScope { block(keys.toSet()) }

    override suspend fun put(key: K, value: V) = Unit

    override suspend fun putAll(from: Map<K, V & Any>) = Unit

    override suspend fun invalidate(key: K) = Unit

    override suspend fun invalidateAll(keys: Iterable<K>) = Unit
}
