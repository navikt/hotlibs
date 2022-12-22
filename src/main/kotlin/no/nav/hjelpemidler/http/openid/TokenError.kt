package no.nav.hjelpemidler.http.openid

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonProperty

data class TokenError(
    @JsonProperty("error") val error: String,
    @JsonProperty("error_description") val errorDescription: String,
    @JsonAnySetter @get:JsonAnyGetter val other: Map<String, Any> = linkedMapOf(),
)
