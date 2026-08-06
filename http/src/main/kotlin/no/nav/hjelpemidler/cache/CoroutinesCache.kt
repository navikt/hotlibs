package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.AsyncCache
import kotlinx.coroutines.CoroutineScope

interface CoroutinesCache<K : Any, V> {
    suspend operator fun get(key: K): V?

    suspend fun getOrLoad(key: K, block: suspend CoroutineScope.(K) -> V): V

    suspend fun getOrLoadAll(
        keys: Iterable<K>,
        block: suspend CoroutineScope.(Set<K>) -> Map<K, V & Any>,
    ): Map<K, V & Any>

    suspend fun put(key: K, value: V)

    suspend fun putAll(from: Map<K, V & Any>)

    suspend fun invalidate(key: K)

    suspend fun invalidateAll(keys: Iterable<K>)
}

fun <K : Any, V : Any> AsyncCache<K, V>.coroutines(): CoroutinesCache<K, V> = CaffeineCoroutinesCache(this)
