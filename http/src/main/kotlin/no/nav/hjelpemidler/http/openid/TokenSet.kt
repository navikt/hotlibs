package no.nav.hjelpemidler.http.openid

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import no.nav.hjelpemidler.time.minus
import no.nav.hjelpemidler.time.nå
import no.nav.hjelpemidler.time.plus
import tools.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import tools.jackson.databind.annotation.JsonNaming
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * @see <a href="https://docs.nais.io/auth/explanations/#texas">Texas</a>
 */
@JsonNaming(SnakeCaseStrategy::class)
data class TokenSet(
    val accessToken: String,
    val expiresIn: Long,
    val tokenType: TokenType = TokenType.BEARER,
    @JsonAnySetter
    @get:JsonAnyGetter
    @get:JsonInclude(Include.NON_EMPTY, content = Include.NON_NULL)
    val additionalProperties: Map<String, Any?> = emptyMap(),
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
    fun expiresIn(leeway: Duration = DEFAULT_LEEWAY): Duration =
        expiresInDuration - leeway

    @JsonIgnore
    fun isExpired(at: Instant = nå(), leeway: Duration = DEFAULT_LEEWAY): Boolean =
        (expiresAt - leeway).let {
            it == at || it.isBefore(at)
        }

    @JsonIgnore
    fun asTokenSetProvider(): TokenSetProvider = TokenSetProvider { this }

    companion object {
        val DEFAULT_LEEWAY: Duration = 1.minutes
    }
}
