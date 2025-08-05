package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.AsyncCache
import kotlinx.coroutines.CoroutineScope

fun <K : Any, V> AsyncCache<K, V>.coroutines(): CoroutinesCache<K, V> =
    CaffeineCoroutinesCache(this)

suspend fun <K : Any, V> AsyncCache<K, V>.getIfPresentAsync(key: K): V? =
    coroutines().getIfPresent(key)

suspend fun <K : Any, V> AsyncCache<K, V>.getAsync(key: K, loader: suspend CoroutineScope.(K) -> V): V =
    coroutines().get(key, loader)

suspend fun <K : Any, V> AsyncCache<K, V>.getAllAsync(
    keys: Iterable<K>,
    loader: suspend CoroutineScope.(Set<K>) -> Map<K, V & Any>,
): Map<K, V & Any> = coroutines().getAll(keys, loader)

suspend fun <K : Any, V> AsyncCache<K, V>.computeIfAbsentAsync(key: K, loader: suspend CoroutineScope.(K) -> V): V? =
    coroutines().computeIfAbsent(key, loader)

suspend fun <K : Any, V> AsyncCache<K, V>.computeIfPresentAsync(
    key: K,
    loader: suspend CoroutineScope.(K, V) -> V,
): V? = coroutines().computeIfPresent(key, loader)

suspend fun <K : Any, V> AsyncCache<K, V>.computeAsync(key: K, loader: suspend CoroutineScope.(K, V?) -> V): V? =
    coroutines().compute(key, loader)

suspend fun <K : Any, V> AsyncCache<K, V>.removeAsync(key: K): V? =
    coroutines().remove(key)
