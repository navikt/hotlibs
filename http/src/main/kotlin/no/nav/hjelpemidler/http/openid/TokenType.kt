package no.nav.hjelpemidler.http.openid

import com.fasterxml.jackson.annotation.JsonProperty

enum class TokenType {
    @JsonProperty("Bearer")
    BEARER,
}
