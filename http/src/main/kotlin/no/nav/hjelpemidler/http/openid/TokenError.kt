package no.nav.hjelpemidler.http.openid

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import tools.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import tools.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class TokenError(
    val error: String,
    val errorDescription: String,
    @JsonAnySetter
    @get:JsonAnyGetter
    @get:JsonInclude(Include.NON_EMPTY, content = Include.NON_NULL)
    val additionalProperties: Map<String, Any?> = emptyMap(),
)
