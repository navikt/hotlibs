package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.AsyncCache
import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import kotlinx.coroutines.supervisorScope
import kotlin.time.Duration
import kotlin.time.toJavaDuration

fun <K : Any, V : Any> Caffeine<K, V>.expireAfterWrite(duration: Duration): Caffeine<K, V> =
    expireAfterWrite(duration.toJavaDuration())

fun <K : Any, V : Any> Caffeine<K, V>.expireAfterAccess(duration: Duration): Caffeine<K, V> =
    expireAfterAccess(duration.toJavaDuration())

fun <K : Any, V : Any> Caffeine<K, V>.refreshAfterWrite(duration: Duration): Caffeine<K, V> =
    refreshAfterWrite(duration.toJavaDuration())

typealias CacheConfigurer<K, V> =
        Caffeine<K, V>.() -> Caffeine<K, V>

@Suppress("UNCHECKED_CAST")
internal fun <K : Any, V : Any> caffeine(): Caffeine<K, V> =
    Caffeine.newBuilder() as Caffeine<K, V>

fun <K : Any, V : Any> createCache(
    configuration: CacheConfigurer<K, V> = { this },
): Cache<K, V> =
    caffeine<K, V>()
        .run(configuration)
        .build()

fun <K : Any, V : Any> createLoadingCache(
    configuration: CacheConfigurer<K, V> = { this },
    loader: (K) -> V,
): LoadingCache<K, V> =
    caffeine<K, V>()
        .run(configuration)
        .build(loader)

fun <K : Any, V : Any> createAsyncCache(
    configuration: CacheConfigurer<K, V> = { this },
): AsyncCache<K, V> =
    caffeine<K, V>()
        .run(configuration)
        .buildAsync()

fun <K : Any, V : Any> createAsyncLoadingCache(
    coroutineScope: CoroutineScope,
    configuration: CacheConfigurer<K, V> = { this },
    loader: suspend (K) -> V,
): AsyncCache<K, V> =
    caffeine<K, V>()
        .run(configuration)
        .buildAsync { key, _ ->
            coroutineScope.future {
                loader(key)
            }
        }

suspend fun <K : Any, V> AsyncCache<K, V>.getIfPresentAsync(key: K): V? =
    getIfPresent(key)?.await()

suspend fun <K : Any, V> AsyncCache<K, V>.getAsync(key: K, loader: suspend (K) -> V): V =
    supervisorScope {
        get(key) { key, _ ->
            future {
                loader(key)
            }
        }.await()
    }
