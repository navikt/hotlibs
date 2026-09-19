package no.nav.hjelpemidler.pip.pdl

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.collections.mapNotNullToSet
import no.nav.hjelpemidler.collections.mapToSet
import no.nav.hjelpemidler.domain.geografi.GeografiskOmråde
import no.nav.hjelpemidler.domain.person.AdressebeskyttelseGradering
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.PersonId
import no.nav.hjelpemidler.domain.person.gjeldende
import no.nav.hjelpemidler.pip.pdl.PdlPipGeografiskTilknytning.Type

data class PdlPipPersonResponse(
    @JsonProperty("aktoerId") val aktørId: AktørId,
    @JsonProperty("person") val person: PdlPipPerson,
    @JsonProperty("identer") val identer: PdlPipIdenter,
    @JsonProperty("geografiskTilknytning") val geografiskTilknytning: PdlPipGeografiskTilknytning,
) {
    @JsonIgnore
    val gjeldendeIdenter: Set<PersonId> = identer
        .filter(PdlPipIdent::isGjeldende)
        .mapToSet(PdlPipIdent::asPersonId)

    val fnr: Fødselsnummer?
        @JsonIgnore
        get() = gjeldendeIdenter
            .filterIsInstance<Fødselsnummer>()
            .firstOrNull()

    val gradering: AdressebeskyttelseGradering
        @JsonIgnore
        get() = person.adressebeskyttelse
            .mapNotNullToSet(PdlPipAdressebeskyttelse::gradering)
            .gjeldende

    val geografiskOmråde: GeografiskOmråde?
        @JsonIgnore
        get() = when (geografiskTilknytning.type) {
            Type.KOMMUNE -> GeografiskOmråde.Kommune(
                geografiskTilknytning.kommune
                    ?: geografiskTilknytning.bydel?.take(4)
            )

            Type.BYDEL -> GeografiskOmråde.Bydel(geografiskTilknytning.bydel)
            Type.UTLAND -> GeografiskOmråde.Land(geografiskTilknytning.land)
            else -> null
        }
}
