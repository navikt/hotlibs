package no.nav.hjelpemidler.http.openid

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

typealias CachePredicate<K, V> = suspend (key: K, value: V) -> Boolean
typealias CacheUpdater<K, V> = suspend (key: K, value: V?) -> V

class Cache<K, V> internal constructor(private val cache: MutableMap<K, V>) : Map<K, V> by cache {
    constructor() : this(mutableMapOf())

    private val mutex = Mutex()

    suspend fun putIf(
        key: K,
        predicate: CachePredicate<K, V>,
        block: CacheUpdater<K, V>,
    ): V = mutex.withLock(cache) {
        val oldValue = cache[key]
        when {
            oldValue == null || predicate(key, oldValue) -> {
                val newValue = block(key, oldValue)
                cache[key] = newValue
                newValue
            }

            else -> oldValue
        }
    }
}
