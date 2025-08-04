package no.nav.hjelpemidler.http.openid

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter

/**
 * @see <a href="https://docs.nais.io/auth/explanations/#texas">Texas</a>
 */
data class TokenIntrospection(
    val active: Boolean,
    val error: String? = null,
    @field:JsonAnyGetter @field:JsonAnySetter val additionalProperties: Map<String, Any?> = mutableMapOf(),
)
