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

private fun <T, K : Any, V : Any> Caffeine<K, V>.setIf(value: T?, block: (T) -> Caffeine<K, V>): Caffeine<K, V> =
    when (value) {
        null -> this
        else -> block(value)
    }

private fun <K : Any, V : Any> Caffeine<K, V>.setIf(value: Boolean, block: () -> Caffeine<K, V>): Caffeine<K, V> =
    when (value) {
        false -> this
        else -> block()
    }

@Suppress("UNCHECKED_CAST")
fun <K : Any, V : Any> Caffeine<Any, Any>.configure(configurer: CacheConfiguration<K, V>.() -> Unit): Caffeine<K, V> {
    val configuration = CacheConfiguration<K, V>().apply(configurer)
    return this
        .setIf(configuration.initialCapacity, ::initialCapacity)
        .setIf(configuration.maximumSize, ::maximumSize)
        .setIf(configuration.maximumWeight, ::maximumWeight)
        .setIf(configuration.weakKeys, ::weakKeys)
        .setIf(configuration.weakValues, ::weakValues)
        .setIf(configuration.softValues, ::softValues)
        .setIf(configuration.expireAfter) {
            expireAfter(it) as Caffeine<Any, Any>
        }
        .setIf(configuration.expireAfterWrite, ::expireAfterWrite)
        .setIf(configuration.expireAfterAccess, ::expireAfterAccess)
        .setIf(configuration.refreshAfterWrite, ::refreshAfterWrite)
        .setIf(configuration.recordStats, ::recordStats) as Caffeine<K, V>
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
