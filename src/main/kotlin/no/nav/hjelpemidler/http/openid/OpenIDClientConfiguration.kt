package no.nav.hjelpemidler.http.openid

import com.github.benmanes.caffeine.cache.Expiry
import io.ktor.http.Parameters
import no.nav.hjelpemidler.cache.CacheConfiguration
import kotlin.time.Duration

class OpenIDClientConfiguration internal constructor() {
    var tokenEndpoint: String = ""
    var clientId: String = ""
    var clientSecret: String? = null
    var cacheConfiguration: OpenIDClientCacheConfiguration? = null
        private set

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

    fun cache(
        leeway: Duration = TokenExpiry.LEEWAY,
        block: OpenIDClientCacheConfiguration.() -> Unit = {},
    ) {
        cacheConfiguration = OpenIDClientCacheConfiguration(leeway).apply(block)
    }
}

class OpenIDClientCacheConfiguration internal constructor(leeway: Duration = TokenExpiry.LEEWAY) :
    CacheConfiguration() {
    var expiry: Expiry<Parameters, TokenSet> = TokenExpiry(leeway)

    fun untilTokenExpiry(leeway: Duration = TokenExpiry.LEEWAY) {
        expiry = TokenExpiry(leeway)
    }
}
