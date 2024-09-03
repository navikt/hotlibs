package no.nav.hjelpemidler.http.openid

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import no.nav.hjelpemidler.time.minus
import no.nav.hjelpemidler.time.nå
import no.nav.hjelpemidler.time.plus
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@JsonNaming(SnakeCaseStrategy::class)
data class TokenSet(
    val tokenType: String,
    val expiresIn: Long,
    val accessToken: String,
    @JsonAnySetter @get:JsonAnyGetter val other: Map<String, Any> = linkedMapOf(),
) {
    @JsonIgnore
    val expiresInDuration = expiresIn.seconds

    @JsonIgnore
    val expiresAt: Instant = nå() + expiresInDuration

    @JsonIgnore
    fun expiresIn(leeway: Duration = TokenExpiry.DEFAULT_LEEWAY): Duration =
        expiresInDuration - leeway

    @JsonIgnore
    fun isExpired(at: Instant = nå(), leeway: Duration = TokenExpiry.DEFAULT_LEEWAY): Boolean =
        (expiresAt - leeway).let {
            it == at || it.isBefore(at)
        }

    companion object {
        fun bearer(expiresIn: Duration, accessToken: String): TokenSet =
            TokenSet(
                tokenType = "Bearer",
                expiresIn = expiresIn.inWholeSeconds,
                accessToken = accessToken,
            )
    }
}
