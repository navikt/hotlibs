package no.nav.hjelpemidler.http.openid

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@JsonNaming(SnakeCaseStrategy::class)
data class TokenSet(
    val tokenType: String,
    val expiresIn: Long,
    val accessToken: String,
    val refreshToken: String? = null,
    @JsonAnySetter @get:JsonAnyGetter val other: Map<String, Any> = linkedMapOf(),
) {
    @JsonIgnore
    val expiresAt: Instant = Helper.now().plus(expiresIn.seconds.toJavaDuration())

    @JsonIgnore
    fun isExpired(at: Instant = Helper.now(), leeway: Duration = Duration.ZERO): Boolean =
        expiresAt
            .minus(leeway.toJavaDuration())
            .run {
                this == at || isBefore(at)
            }
}
