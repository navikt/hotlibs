package no.nav.hjelpemidler.pip.pdl

import no.nav.hjelpemidler.domain.person.AdressebeskyttelseGradering
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer

fun lagPipPersonResponse(
    fnr: Fødselsnummer,
    geografiskTilknytning: PdlPipGeografiskTilknytning = PdlPipGeografiskTilknytning(
        type = PdlPipGeografiskTilknytning.Type.KOMMUNE,
        kommune = "0301",
        bydel = "030105",
        land = null,
        regel = null,
    ),
): PdlPipPersonResponse {
    val aktørId = AktørId("1234567891011")
    return PdlPipPersonResponse(
        aktørId = aktørId,
        person = PdlPipPerson(
            adressebeskyttelse = listOf(PdlPipAdressebeskyttelse(AdressebeskyttelseGradering.UGRADERT)),
            fødselsdato = emptyList(),
            dødsfall = emptyList(),
            familierelasjoner = emptyList(),
            rettsligHandleevne = emptyList(),
        ),
        identer = PdlPipIdenter(
            listOf(
                PdlPipIdent(ident = aktørId.value, gruppe = PdlPipIdent.Gruppe.AKTORID, isHistorisk = false),
                PdlPipIdent(ident = fnr.value, gruppe = PdlPipIdent.Gruppe.FOLKEREGISTERIDENT, isHistorisk = false),
            )
        ),
        geografiskTilknytning = geografiskTilknytning,
    )
}
