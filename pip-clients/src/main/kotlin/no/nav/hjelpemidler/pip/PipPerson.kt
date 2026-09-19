package no.nav.hjelpemidler.pip

import no.nav.hjelpemidler.domain.geografi.GeografiskOmråde
import no.nav.hjelpemidler.domain.person.AdressebeskyttelseGradering
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.PersonId
import no.nav.hjelpemidler.pip.pdl.PdlPipPersonResponse
import java.time.LocalDate

data class PipPerson(
    val aktørId: AktørId,
    val fnr: Fødselsnummer?,
    val gjeldendeIdenter: Set<PersonId>,
    val fødselsdato: LocalDate?,
    val dødsdato: LocalDate?,
    val geografiskOmråde: GeografiskOmråde?,
    val adressebeskyttelseGradering: AdressebeskyttelseGradering,
    val isSkjermet: Boolean,
) {
    val isAdressebeskyttelseGradert: Boolean get() = adressebeskyttelseGradering.isGradert

    constructor(pipPersonResponse: PdlPipPersonResponse, isSkjermet: Boolean) : this(
        aktørId = pipPersonResponse.aktørId,
        fnr = pipPersonResponse.fnr,
        gjeldendeIdenter = pipPersonResponse.gjeldendeIdenter,
        fødselsdato = pipPersonResponse.person.fødselsdato.firstOrNull()?.fødselsdato,
        dødsdato = pipPersonResponse.person.dødsfall.firstOrNull()?.dødsdato,
        geografiskOmråde = pipPersonResponse.geografiskOmråde,
        adressebeskyttelseGradering = pipPersonResponse.adressebeskyttelseGradering,
        isSkjermet = isSkjermet,
    )
}
