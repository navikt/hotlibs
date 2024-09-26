package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.Caffeine
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

internal fun Caffeine<Any, Any>.configure(configuration: CacheConfiguration): Caffeine<Any, Any> =
    this
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

fun Caffeine<Any, Any>.configure(block: CacheConfiguration.() -> Unit): Caffeine<Any, Any> =
    configure(CacheConfiguration().apply(block))

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
