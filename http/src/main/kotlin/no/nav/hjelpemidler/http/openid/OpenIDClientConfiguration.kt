package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.Expiry
import io.ktor.http.Parameters
import no.nav.hjelpemidler.cache.CacheConfiguration
import kotlin.time.Duration

class OpenIDClientConfiguration internal constructor() {
    internal var expiry: Expiry<Parameters, TokenSet> = ExpireImmediately
    internal val cacheConfiguration: CacheConfiguration = CacheConfiguration()
    fun cache(
        leeway: Duration = TokenExpiry.DEFAULT_LEEWAY,
        expiry: Expiry<Parameters, TokenSet> = TokenExpiry(leeway),
        block: CacheConfiguration.() -> Unit = {},
    ) {
        this.expiry = expiry
        this.cacheConfiguration.apply(block)
    }
}
