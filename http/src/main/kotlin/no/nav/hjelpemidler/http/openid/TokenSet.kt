package no.nav.hjelpemidler.http.openid

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import tools.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import tools.jackson.databind.annotation.JsonNaming
import kotlin.time.Duration

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
    @JsonIgnore
    constructor(accessToken: String, expiresIn: Duration) : this(
        accessToken = accessToken,
        expiresIn = expiresIn.inWholeSeconds,
        tokenType = TokenType.BEARER,
    )

    fun asTokenSetProvider(): TokenSetProvider = TokenSetProvider { this }
}
