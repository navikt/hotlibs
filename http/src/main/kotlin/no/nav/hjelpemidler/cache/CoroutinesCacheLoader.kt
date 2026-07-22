package no.nav.hjelpemidler.cache

fun interface CoroutinesCacheLoader<K : Any, V> {
    suspend fun load(key: K): V
    suspend fun loadAll(keys: Set<K>): Map<K, V & Any> = throw UnsupportedOperationException()
    suspend fun reload(key: K, oldValue: V & Any): V = load(key)
}
