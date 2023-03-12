package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.Expiry
import io.ktor.http.Parameters
import no.nav.hjelpemidler.cache.CacheConfiguration
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class OpenIDClientConfiguration internal constructor() {
    var tokenEndpoint: String = ""
    var clientId: String = ""
    var clientSecret: String? = null

    fun azureAD() {
        tokenEndpoint = AzureADEnvironmentVariable.AZURE_OPENID_CONFIG_TOKEN_ENDPOINT
        clientId = AzureADEnvironmentVariable.AZURE_APP_CLIENT_ID
        clientSecret = AzureADEnvironmentVariable.AZURE_APP_CLIENT_SECRET
    }

    fun tokenX() {
        tokenEndpoint = TokenXEnvironmentVariable.TOKEN_X_TOKEN_ENDPOINT
        clientId = TokenXEnvironmentVariable.TOKEN_X_CLIENT_ID
        clientSecret = null
    }

    internal val cacheConfiguration: CacheConfiguration = CacheConfiguration()
    internal var expiry: Expiry<Parameters, TokenSet> = IMMEDIATELY
    internal val cache: Boolean get() = expiry !== IMMEDIATELY

    fun cache(
        leeway: Duration = TokenExpiry.DEFAULT_LEEWAY,
        expiry: Expiry<Parameters, TokenSet> = TokenExpiry(leeway),
        block: CacheConfiguration.() -> Unit = {},
    ) {
        this.expiry = expiry
        cacheConfiguration.apply(block)
    }

    companion object {
        internal val IMMEDIATELY: Expiry<Parameters, TokenSet> = TokenExpiry(0.seconds)
    }
}
