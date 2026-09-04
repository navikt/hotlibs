package no.nav.hjelpemidler.pip.pdl

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class PipIdent(
    @JsonProperty("ident") val ident: String,
    @JsonProperty("historisk") val historisk: Boolean,
    @JsonProperty("gruppe") val gruppe: PipIdentGruppe,
) {
    val isGjeldende: Boolean @JsonIgnore get() = !historisk
    val isAktørId: Boolean @JsonIgnore get() = gruppe == PipIdentGruppe.AKTORID
    val isFolkeregisterident: Boolean @JsonIgnore get() = gruppe == PipIdentGruppe.FOLKEREGISTERIDENT
}
