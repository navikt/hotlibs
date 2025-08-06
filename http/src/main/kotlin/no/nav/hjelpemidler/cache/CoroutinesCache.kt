package no.nav.hjelpemidler.cache

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred

interface CoroutinesCache<K : Any, V> {
    suspend fun getIfPresent(key: K): V?
    suspend fun get(key: K, loader: suspend CoroutineScope.(K) -> V): V
    suspend fun getAll(keys: Iterable<K>, loader: suspend CoroutineScope.(Set<K>) -> Map<K, V & Any>): Map<K, V & Any>
    suspend fun put(key: K, value: V)
    suspend fun computeIfAbsent(key: K, loader: suspend CoroutineScope.(K) -> V): V
    suspend fun computeIfPresent(key: K, loader: suspend CoroutineScope.(K, V) -> V): V?
    suspend fun compute(key: K, loader: suspend CoroutineScope.(K, V?) -> V): V?
    suspend fun remove(key: K): V?
    suspend fun asMap(): Map<K, Deferred<V>>
}
