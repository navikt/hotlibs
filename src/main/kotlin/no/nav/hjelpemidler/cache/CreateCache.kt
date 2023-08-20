package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.Scheduler

fun createCache(
    block: CacheConfiguration.() -> Unit = {},
): Caffeine<Any, Any> =
    Caffeine.newBuilder()
        .scheduler(Scheduler.systemScheduler())
        .configure(block)

internal fun createCache(
    configuration: CacheConfiguration,
): Caffeine<Any, Any> =
    Caffeine.newBuilder()
        .scheduler(Scheduler.systemScheduler())
        .configure(configuration)
