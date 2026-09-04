package no.nav.hjelpemidler.pip.pdl

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.domain.person.AdressebeskyttelseGradering
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.gjeldende
import no.nav.hjelpemidler.pip.pdl.PipGeografiskTilknytning.Type

data class PipPersonResponse(
    @JsonProperty("aktoerId") val aktørId: AktørId,
    @JsonProperty("person") val person: PipPerson,
    @JsonProperty("identer") val identer: PipIdenter,
    @JsonProperty("geografiskTilknytning") val geografiskTilknytning: PipGeografiskTilknytning,
) {
    val fnr: Fødselsnummer
        @JsonIgnore get() = identer
            .asSequence()
            .filter(PipIdent::isGjeldende)
            .filter(PipIdent::isFolkeregisterident)
            .map(PipIdent::ident)
            .map(::Fødselsnummer)
            .single()

    val gradering: AdressebeskyttelseGradering
        @JsonIgnore get() = person.adressebeskyttelse
            .mapNotNullTo(sortedSetOf(), PipAdressebeskyttelse::gradering)
            .gjeldende

    val geografiskOmråde: String?
        @JsonIgnore get() = when (geografiskTilknytning.type) {
            Type.KOMMUNE -> geografiskTilknytning.kommune ?: geografiskTilknytning.bydel?.take(4)
            Type.BYDEL -> geografiskTilknytning.bydel
            Type.UTLAND -> geografiskTilknytning.land ?: "UTLAND"
            Type.UDEFINERT -> null
            else -> null
        }
}
