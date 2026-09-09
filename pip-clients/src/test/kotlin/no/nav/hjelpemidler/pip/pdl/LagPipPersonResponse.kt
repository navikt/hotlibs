package no.nav.hjelpemidler.pip.pdl

import no.nav.hjelpemidler.domain.person.AdressebeskyttelseGradering
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer

fun lagPipPersonResponse(
    fnr: Fødselsnummer,
    geografiskTilknytning: PipGeografiskTilknytning = PipGeografiskTilknytning(
        type = PipGeografiskTilknytning.Type.KOMMUNE,
        kommune = "0301",
        bydel = "030105",
        land = null,
        regel = null,
    ),
): PipPersonResponse {
    val aktørId = AktørId("1234567891011")
    return PipPersonResponse(
        aktørId = aktørId,
        person = PipPerson(
            adressebeskyttelse = listOf(PipAdressebeskyttelse(AdressebeskyttelseGradering.UGRADERT)),
            fødselsdato = emptyList(),
            dødsfall = emptyList(),
            familierelasjoner = emptyList(),
            rettsligHandleevne = emptyList(),
        ),
        identer = PipIdenter(
            listOf(
                PipIdent(ident = aktørId.value, gruppe = PipIdent.Gruppe.AKTORID, isHistorisk = false),
                PipIdent(ident = fnr.value, gruppe = PipIdent.Gruppe.FOLKEREGISTERIDENT, isHistorisk = false),
            )
        ),
        geografiskTilknytning = geografiskTilknytning,
    )
}
