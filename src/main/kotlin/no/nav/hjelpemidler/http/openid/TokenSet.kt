package no.nav.hjelpemidler.http.openid

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.client.plugins.auth.providers.BearerTokens

data class TokenSet(
    @JsonProperty("token_type") val tokenType: String,
    @JsonProperty("expires_in") val expiresIn: Long,
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("refresh_token") val refreshToken: String? = null,
    @JsonAnySetter @get:JsonAnyGetter val other: Map<String, Any> = linkedMapOf(),
) {
    @JsonIgnore
    fun toBearerTokens(): BearerTokens = BearerTokens(accessToken, refreshToken ?: "")
}
