package no.nav.hjelpemidler.http.openid

import com.auth0.jwt.interfaces.DecodedJWT

interface OpenIDClient {
    /**
     * Utsted Machine-To-Machine-token (M2M-token) med [scope].
     */
    suspend fun grant(scope: String): TokenSet

    /**
     * Utsted On-Behalf-Of-token (OBO-token) med [scope].
     */
    suspend fun grant(scope: String, onBehalfOf: String): TokenSet

    /**
     * Utsted On-Behalf-Of-token (OBO-token) med [scope].
     */
    suspend fun grant(scope: String, onBehalfOf: DecodedJWT): TokenSet = grant(scope, onBehalfOf.token)

    /**
     * [TokenSetProvider] som alltid gjør grant med [scope].
     */
    fun withScope(scope: String): TokenSetProvider = TokenSetProvider { grant(scope) }
}
