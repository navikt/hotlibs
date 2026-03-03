package no.nav.hjelpemidler.http.openid

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import tools.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import tools.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class TokenError(
    val error: String,
    val errorDescription: String,
    @field:JsonAnyGetter @field:JsonAnySetter val additionalProperties: Map<String, Any?> = mutableMapOf(),
)
