package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.Expiry
import io.ktor.http.Parameters
import no.nav.hjelpemidler.cache.CacheConfiguration
import no.nav.hjelpemidler.configuration.EntraIDEnvironmentVariable
import no.nav.hjelpemidler.configuration.MaskinportenEnvironmentVariable
import kotlin.time.Duration

class OpenIDClientConfiguration internal constructor() {
    var identityProvider: IdentityProvider? = null
    var tokenEndpoint: String = ""
    var clientId: String = ""
    var clientSecret: String? = null

    fun entraIDEnvironmentConfiguration() {
        identityProvider = IdentityProvider.ENTRA_ID
        tokenEndpoint = EntraIDEnvironmentVariable.AZURE_OPENID_CONFIG_TOKEN_ENDPOINT
        clientId = EntraIDEnvironmentVariable.AZURE_APP_CLIENT_ID
        clientSecret = EntraIDEnvironmentVariable.AZURE_APP_CLIENT_SECRET
    }

    fun maskinportenEnvironmentConfiguration() {
        identityProvider = IdentityProvider.MASKINPORTEN
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
