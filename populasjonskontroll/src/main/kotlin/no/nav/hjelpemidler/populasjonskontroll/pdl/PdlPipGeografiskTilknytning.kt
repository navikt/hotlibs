package no.nav.hjelpemidler.populasjonskontroll.pdl

import com.fasterxml.jackson.annotation.JsonProperty

data class PdlPipGeografiskTilknytning(
    @JsonProperty("gtType") val type: Type?,
    @JsonProperty("gtKommune") val kommune: String?,
    @JsonProperty("gtBydel") val bydel: String?,
    @JsonProperty("gtLand") val land: String?,
    @JsonProperty("regel") val regel: String?,
) {
    enum class Type {
        KOMMUNE,
        BYDEL,
        UTLAND,
        UDEFINERT,
    }
}
