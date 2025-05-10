package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.AsyncCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future

suspend fun <K : Any, V> AsyncCache<K, V>.getIfPresentAsync(key: K): V? =
    getIfPresent(key)?.await()

suspend fun <K : Any, V> AsyncCache<K, V>.getAsync(
    key: K,
    loader: suspend CoroutineScope.(K) -> V,
): V = coroutineScope {
    get(key) { key, _ ->
        future { loader(key) }
    }.await()
}

suspend fun <K : Any, V> AsyncCache<K, V>.getAllAsync(
    keys: Iterable<K>,
    loader: suspend CoroutineScope.(Set<K>) -> Map<K, V & Any>,
): Map<K, V & Any> = coroutineScope {
    getAll(keys) { keys, _ ->
        future { loader(keys) }
    }.await()
}

suspend fun <K : Any, V> AsyncCache<K, V>.computeAsync(
    key: K,
    loader: suspend CoroutineScope.(K, V?) -> V,
): V? = coroutineScope {
    asMap().compute(key) { key, value ->
        future { loader(key, value?.await()) }
    }?.await()
}

suspend fun <K : Any, V> AsyncCache<K, V>.removeAsync(key: K): V? =
    asMap().remove(key)?.await()
