package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.AsyncCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred

interface CoroutinesCache<K : Any, V : Any> {
    suspend fun getIfPresent(key: K): V?
    suspend fun get(key: K, loader: suspend CoroutineScope.(K) -> V): V = computeIfAbsent(key, loader)
    suspend fun getAll(keys: Iterable<K>, loader: suspend CoroutineScope.(Set<K>) -> Map<K, V>): Map<K, V>
    fun put(key: K, value: V)
    suspend fun computeIfAbsent(key: K, loader: suspend CoroutineScope.(K) -> V): V
    suspend fun computeIfPresent(key: K, loader: suspend CoroutineScope.(K, V) -> V): V?
    suspend fun compute(key: K, loader: suspend CoroutineScope.(K, V?) -> V): V
    suspend fun remove(key: K): V?
    fun asMap(): Map<K, Deferred<V>>
}

fun <K : Any, V : Any> AsyncCache<K, V>.coroutines(): CoroutinesCache<K, V> = CaffeineCoroutinesCache(this)
