package no.nav.hjelpemidler.cache

import com.github.benmanes.caffeine.cache.Caffeine

fun <K : Any, V : Any> createCache(
    configurer: CacheConfiguration<K, V>.() -> Unit = {},
): Caffeine<K, V> =
    Caffeine.newBuilder().configure(configurer)
