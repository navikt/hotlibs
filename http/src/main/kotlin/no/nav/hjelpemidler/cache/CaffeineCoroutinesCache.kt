package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.AsyncCache
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import java.util.AbstractMap.SimpleImmutableEntry
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executor

internal class CaffeineCoroutinesCache<K : Any, V : Any>(private val wrapped: AsyncCache<K, V>) :
    CoroutinesCache<K, V> {
    private val dispatchers = ConcurrentHashMap<Executor, CoroutineDispatcher>()
    private fun dispatcher(executor: Executor): CoroutineDispatcher =
        dispatchers.computeIfAbsent(executor, Executor::asCoroutineDispatcher)

    override suspend fun getIfPresent(key: K): V? = wrapped.getIfPresent(key)?.await()

    override suspend fun getAll(
        keys: Iterable<K>,
        loader: suspend CoroutineScope.(Set<K>) -> Map<K, V>,
    ): Map<K, V> = coroutineScope {
        wrapped
            .getAll(keys) { keys, executor -> future(dispatcher(executor)) { loader(keys) } }
            .await()
    }

    override fun put(key: K, value: V) = wrapped.put(key, CompletableFuture.completedFuture(value))

    override suspend fun computeIfAbsent(key: K, loader: suspend CoroutineScope.(K) -> V): V = coroutineScope {
        wrapped
            .get(key) { key, executor -> future(dispatcher(executor)) { loader(key) } }
            .await()
    }

    override suspend fun computeIfPresent(key: K, loader: suspend CoroutineScope.(K, V) -> V): V? = coroutineScope {
        val currentValue = getIfPresent(key) ?: return@coroutineScope null
        loader(key, currentValue).also { put(key, it) }
    }

    override suspend fun compute(key: K, loader: suspend CoroutineScope.(K, V?) -> V): V = coroutineScope {
        val currentValue = getIfPresent(key)
        loader(key, currentValue).also { put(key, it) }
    }

    override suspend fun remove(key: K): V? = wrapped.asMap().remove(key)?.await()

    override fun asMap(): Map<K, Deferred<V>> = object : AbstractMap<K, Deferred<V>>() {
        private val delegate = wrapped.asMap()

        override val keys: Set<K> get() = delegate.keys

        override val entries: Set<Map.Entry<K, Deferred<V>>>
            get() = delegate.entries.mapTo(LinkedHashSet()) { entry ->
                SimpleImmutableEntry(
                    entry.key,
                    entry.value.asDeferred(),
                )
            }

        override fun get(key: K): Deferred<V>? = delegate[key]?.asDeferred()

        override fun isEmpty(): Boolean = delegate.isEmpty()

        override val size: Int get() = delegate.size
    }
}
