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
    val accessToken: String,
    val expiresIn: Long,
    val tokenType: TokenType = TokenType.BEARER,
    @field:JsonAnyGetter @field:JsonAnySetter val additionalProperties: Map<String, Any?> = mutableMapOf(),
) {
    val expiresInDuration: Duration @JsonIgnore get() = expiresIn.seconds
    val expiresAt: Instant @JsonIgnore get() = nå() + expiresInDuration

    @JsonIgnore
    constructor(accessToken: String, expiresIn: Duration) : this(
        accessToken = accessToken,
        expiresIn = expiresIn.inWholeSeconds,
        tokenType = TokenType.BEARER,
    )

    @JsonIgnore
    fun expiresIn(leeway: Duration = TokenExpiry.DEFAULT_LEEWAY): Duration =
        expiresInDuration - leeway

    @JsonIgnore
    fun isExpired(at: Instant = nå(), leeway: Duration = TokenExpiry.DEFAULT_LEEWAY): Boolean =
        (expiresAt - leeway).let {
            it == at || it.isBefore(at)
        }
}
