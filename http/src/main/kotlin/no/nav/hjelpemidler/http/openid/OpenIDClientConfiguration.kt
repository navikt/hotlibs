package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.Expiry
import io.ktor.http.Parameters
import no.nav.hjelpemidler.cache.CacheConfiguration
import no.nav.hjelpemidler.configuration.EntraIDEnvironmentVariable
import no.nav.hjelpemidler.configuration.MaskinportenEnvironmentVariable
import no.nav.hjelpemidler.configuration.TokenXEnvironmentVariable
import kotlin.time.Duration

class OpenIDClientConfiguration internal constructor() {
    var tokenEndpoint: String = ""
    var clientId: String = ""
    var clientSecret: String? = null

    fun entraIDEnvironmentConfiguration() {
        tokenEndpoint = EntraIDEnvironmentVariable.AZURE_OPENID_CONFIG_TOKEN_ENDPOINT
        clientId = EntraIDEnvironmentVariable.AZURE_APP_CLIENT_ID
        clientSecret = EntraIDEnvironmentVariable.AZURE_APP_CLIENT_SECRET
    }

    fun tokenXEnvironmentConfiguration() {
        tokenEndpoint = TokenXEnvironmentVariable.TOKEN_X_TOKEN_ENDPOINT
        clientId = TokenXEnvironmentVariable.TOKEN_X_CLIENT_ID
        clientSecret = null
    }

    fun maskinportenEnvironmentConfiguration() {
        tokenEndpoint = MaskinportenEnvironmentVariable.MASKINPORTEN_TOKEN_ENDPOINT
        // NB! Disse er ikke relevante for Maskinporten, men kreves av [no.nav.hjelpemidler.http.openid.OpenIDClient].
        clientId = MaskinportenEnvironmentVariable.MASKINPORTEN_CLIENT_ID
        clientSecret = ""
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
