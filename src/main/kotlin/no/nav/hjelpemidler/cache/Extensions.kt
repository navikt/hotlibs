package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.AsyncCache
import com.github.benmanes.caffeine.cache.AsyncLoadingCache
import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import kotlinx.coroutines.supervisorScope
import kotlin.time.Duration
import kotlin.time.toJavaDuration

private fun <T> Caffeine<Any, Any>.setIf(
    value: T?,
    block: (T) -> Caffeine<Any, Any>,
): Caffeine<Any, Any> =
    when (value) {
        null -> this
        else -> block(value)
    }

private fun Caffeine<Any, Any>.setIf(
    value: Boolean,
    block: () -> Caffeine<Any, Any>,
): Caffeine<Any, Any> =
    when (value) {
        false -> this
        else -> block()
    }

fun Caffeine<Any, Any>.configure(
    configurer: CacheConfiguration.() -> Unit,
): Caffeine<Any, Any> {
    val configuration = CacheConfiguration().apply(configurer)
    return this
        .setIf(configuration.initialCapacity, ::initialCapacity)
        .setIf(configuration.maximumSize, ::maximumSize)
        .setIf(configuration.maximumWeight, ::maximumWeight)
        .setIf(configuration.weakKeys, ::weakKeys)
        .setIf(configuration.weakValues, ::weakValues)
        .setIf(configuration.softValues, ::softValues)
        .setIf(configuration.expireAfterWrite, ::expireAfterWrite)
        .setIf(configuration.expireAfterAccess, ::expireAfterAccess)
        .setIf(configuration.refreshAfterWrite, ::refreshAfterWrite)
        .setIf(configuration.recordStats, ::recordStats)
}

fun <K : Any, V : Any> Caffeine<K, V>.expireAfterWrite(
    duration: Duration,
): Caffeine<K, V> =
    expireAfterWrite(duration.toJavaDuration())

fun <K : Any, V : Any> Caffeine<K, V>.expireAfterAccess(
    duration: Duration,
): Caffeine<K, V> =
    expireAfterAccess(duration.toJavaDuration())

fun <K : Any, V : Any> Caffeine<K, V>.refreshAfterWrite(
    duration: Duration,
): Caffeine<K, V> =
    refreshAfterWrite(duration.toJavaDuration())

fun <K : Any, V : Any> Caffeine<K, V>.buildAsync(
    coroutineScope: CoroutineScope,
    loader: suspend (K) -> V,
): AsyncLoadingCache<K, V> =
    buildAsync { key, _ ->
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
