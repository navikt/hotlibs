package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.Caffeine

fun createCache(
    configurer: CacheConfiguration.() -> Unit = {},
): Caffeine<Any, Any> =
    Caffeine.newBuilder().configure(configurer)
