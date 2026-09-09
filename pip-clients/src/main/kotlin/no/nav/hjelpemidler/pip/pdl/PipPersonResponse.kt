package no.nav.hjelpemidler.pip.pdl

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.collections.mapNotNullToSet
import no.nav.hjelpemidler.collections.mapToSet
import no.nav.hjelpemidler.domain.person.AdressebeskyttelseGradering
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.PersonId
import no.nav.hjelpemidler.domain.person.gjeldende
import no.nav.hjelpemidler.pip.pdl.PipGeografiskTilknytning.Type

data class PipPersonResponse(
    @JsonProperty("aktoerId") val aktørId: AktørId,
    @JsonProperty("person") val person: PipPerson,
    @JsonProperty("identer") val identer: PipIdenter,
    @JsonProperty("geografiskTilknytning") val geografiskTilknytning: PipGeografiskTilknytning,
) {
    @JsonIgnore
    val gjeldendeIdenter: Set<PersonId> = identer
        .filter(PipIdent::isGjeldende)
        .mapToSet(PipIdent::asPersonId)

    val fnr: Fødselsnummer?
        @JsonIgnore
        get() = gjeldendeIdenter
            .filterIsInstance<Fødselsnummer>()
            .firstOrNull()

    val gradering: AdressebeskyttelseGradering
        @JsonIgnore
        get() = person.adressebeskyttelse
            .mapNotNullToSet(PipAdressebeskyttelse::gradering)
            .gjeldende

    val geografiskOmråde: String?
        @JsonIgnore
        get() = when (geografiskTilknytning.type) {
            Type.KOMMUNE -> geografiskTilknytning.kommune ?: geografiskTilknytning.bydel?.take(4)
            Type.BYDEL -> geografiskTilknytning.bydel
            Type.UTLAND -> geografiskTilknytning.land ?: "UTLAND"
            Type.UDEFINERT -> null
            else -> null
        }
}
