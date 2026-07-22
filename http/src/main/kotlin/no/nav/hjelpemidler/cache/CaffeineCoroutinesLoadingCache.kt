package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import kotlinx.coroutines.future.await

internal class CaffeineCoroutinesLoadingCache<K : Any, V>(private val wrapped: AsyncLoadingCache<K, V>) :
    CoroutinesLoadingCache<K, V>, CoroutinesCache<K, V> by CaffeineCoroutinesCache(wrapped) {
    override suspend fun get(key: K): V = wrapped.get(key).await()
    override suspend fun getAll(keys: Iterable<K>): Map<K, V & Any> = wrapped.getAll(keys).await()
}
