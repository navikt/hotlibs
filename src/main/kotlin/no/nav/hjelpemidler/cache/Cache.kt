package no.nav.hjelpemidler.cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Cache<K : Any, V> internal constructor(
    private val cache: MutableMap<K, V>,
) : Map<K, V> by cache {
    constructor() : this(mutableMapOf())

    private val mutex = Mutex()

    suspend fun computeIfAbsent(
        key: K,
        block: suspend (key: K) -> V,
    ): V = mutex.withLock(cache) {
        when (val oldValue = cache[key]) {
            null -> {
                val newValue = block(key)
                cache[key] = newValue
                newValue
            }

            else -> oldValue
        }
    }

    suspend fun computeIf(
        key: K,
        predicate: suspend (key: K, oldValue: V) -> Boolean,
        block: suspend (key: K, oldValue: V?) -> V,
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
