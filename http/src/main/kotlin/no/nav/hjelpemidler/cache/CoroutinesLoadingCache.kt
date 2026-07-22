package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.AsyncLoadingCache

interface CoroutinesLoadingCache<K : Any, V> : CoroutinesCache<K, V> {
    suspend fun get(key: K): V
    suspend fun getAll(keys: Iterable<K>): Map<K, V & Any>
}

fun <K : Any, V : Any> AsyncLoadingCache<K, V>.coroutines(): CoroutinesLoadingCache<K, V> =
    CaffeineCoroutinesLoadingCache(this)
