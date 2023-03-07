package no.nav.hjelpemidler.http.openid

import com.auth0.jwt.interfaces.DecodedJWT
import io.ktor.http.ParametersBuilder

interface OpenIDClient {
    suspend fun grant(builder: ParametersBuilder.() -> Unit): TokenSet

    suspend fun grant(scope: String): TokenSet = grant {
        grantType(GrantType.CLIENT_CREDENTIALS)
        scope(scope)
    }

    suspend fun grant(scope: String, onBehalfOf: String): TokenSet = grant {
        grantType(GrantType.JWT_BEARER)
        scope(scope)
        assertion(onBehalfOf)
        requestedTokenUse("on_behalf_of")
    }

    suspend fun grant(scope: String, onBehalfOf: DecodedJWT): TokenSet = grant {
        grantType(GrantType.JWT_BEARER)
        scope(scope)
        assertion(onBehalfOf.token)
        requestedTokenUse("on_behalf_of")
    }

    fun ParametersBuilder.grantType(value: String) = append("grant_type", value)
    fun ParametersBuilder.clientId(value: String) = append("client_id", value)
    fun ParametersBuilder.clientSecret(value: String) = append("client_secret", value)
    fun ParametersBuilder.assertion(value: String) = append("assertion", value)
    fun ParametersBuilder.scope(value: String) = append("scope", value)
    fun ParametersBuilder.requestedTokenUse(value: String) = append("requested_token_use", value)
}
