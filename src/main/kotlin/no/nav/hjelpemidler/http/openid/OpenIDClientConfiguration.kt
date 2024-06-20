package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.Expiry
import io.ktor.http.Parameters
import no.nav.hjelpemidler.cache.CacheConfiguration
import no.nav.hjelpemidler.configuration.EntraID
import no.nav.hjelpemidler.configuration.TokenX
import kotlin.time.Duration

class OpenIDClientConfiguration internal constructor() {
    var tokenEndpoint: String = ""
    var clientId: String = ""
    var clientSecret: String? = null

    fun azureADEnvironmentConfiguration() {
        tokenEndpoint = EntraID.AZURE_OPENID_CONFIG_TOKEN_ENDPOINT
        clientId = EntraID.AZURE_APP_CLIENT_ID
        clientSecret = EntraID.AZURE_APP_CLIENT_SECRET
    }

    fun tokenXEnvironmentConfiguration() {
        tokenEndpoint = TokenX.TOKEN_X_TOKEN_ENDPOINT
        clientId = TokenX.TOKEN_X_CLIENT_ID
        clientSecret = null
    }

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
