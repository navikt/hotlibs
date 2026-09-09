package no.nav.hjelpemidler.pip.pdl

import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.domain.person.AdressebeskyttelseGradering
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import java.time.LocalDate

data class PipPerson(
    @JsonProperty("adressebeskyttelse") val adressebeskyttelse: List<PipAdressebeskyttelse>,
    @JsonProperty("foedsel") val fødselsdato: List<PipFødselsdato>,
    @JsonProperty("doedsfall") val dødsfall: List<PipDødsfall>,
    @JsonProperty("familierelasjoner") val familierelasjoner: List<PipFamilierelasjon>,
    @JsonProperty("rettsligHandleevne") val rettsligHandleevne: List<PipRettsligHandleevne>,
)

data class PipAdressebeskyttelse(@JsonProperty("gradering") val gradering: AdressebeskyttelseGradering?)

data class PipFødselsdato(@JsonProperty("foedselsdato") val fødselsdato: LocalDate)

data class PipDødsfall(@JsonProperty("doedsdato") val dødsdato: LocalDate)

data class PipFamilierelasjon(
    @JsonProperty("relatertPersonsIdent") val relatertPersonsIdent: Fødselsnummer?,
    @JsonProperty("relatertPersonsRolle") val relatertPersonsRolle: String?,
    @JsonProperty("minRolleForPerson") val minRolleForPerson: String?,
)

data class PipRettsligHandleevne(@JsonProperty("rettsligHandleevneomfang") val omfang: String?)
