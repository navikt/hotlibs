package no.nav.hjelpemidler.norg

import com.fasterxml.jackson.annotation.JsonProperty

data class NorgArbeidsfordelingRequest(
    @JsonProperty("geografiskOmraade")
    val geografiskOmråde: String,
) {
    val tema = "HJE"
    val temagruppe = "HJLPM"
}
