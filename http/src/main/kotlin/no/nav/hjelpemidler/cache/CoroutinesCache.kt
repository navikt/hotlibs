package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.AsyncCache
import kotlinx.coroutines.CoroutineScope

interface CoroutinesCache<K : Any, V> {
    suspend fun getIfPresent(key: K): V?
    suspend fun get(key: K, loader: suspend CoroutineScope.(K) -> V): V
    suspend fun getAll(keys: Iterable<K>, loader: suspend CoroutineScope.(Set<K>) -> Map<K, V & Any>): Map<K, V & Any>
    fun put(key: K, value: V)
    fun invalidate(key: K)
}

fun <K : Any, V> AsyncCache<K, V>.coroutines(): CoroutinesCache<K, V> = CaffeineCoroutinesCache(this)
