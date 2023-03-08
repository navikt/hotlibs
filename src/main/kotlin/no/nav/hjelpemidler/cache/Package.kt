package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.Scheduler

fun createCache(
    configurer: CacheConfiguration.() -> Unit = {},
): Caffeine<Any, Any> =
    Caffeine.newBuilder()
        .scheduler(Scheduler.systemScheduler())
        .configure(configurer)
