package no.nav.hjelpemidler.pip.pdl

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.NPID
import no.nav.hjelpemidler.domain.person.PersonId

data class PipIdenter(@JsonProperty("identer") val identer: List<PipIdent>) : Iterable<PipIdent> by identer

data class PipIdent(
    @JsonProperty("ident") val ident: String,
    @JsonProperty("gruppe") val gruppe: Gruppe,
    @JsonProperty("historisk") val isHistorisk: Boolean,
) {
    val isGjeldende: Boolean @JsonIgnore get() = !isHistorisk

    fun asPersonId(): PersonId = when (gruppe) {
        Gruppe.AKTORID -> AktørId(ident)
        Gruppe.FOLKEREGISTERIDENT -> Fødselsnummer(ident)
        Gruppe.NPID -> NPID(ident)
    }

    enum class Gruppe {
        AKTORID,
        FOLKEREGISTERIDENT,
        NPID,
    }
}
