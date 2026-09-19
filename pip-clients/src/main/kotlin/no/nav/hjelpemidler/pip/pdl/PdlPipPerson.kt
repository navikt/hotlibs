package no.nav.hjelpemidler.pip.pdl

import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.domain.person.AdressebeskyttelseGradering
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import java.time.LocalDate

data class PdlPipPerson(
    @JsonProperty("adressebeskyttelse") val adressebeskyttelse: List<PdlPipAdressebeskyttelse>,
    @JsonProperty("foedsel") val fødselsdato: List<PdlPipFødselsdato>,
    @JsonProperty("doedsfall") val dødsfall: List<PdlPipDødsfall>,
    @JsonProperty("familierelasjoner") val familierelasjoner: List<PdlPipFamilierelasjon>,
    @JsonProperty("rettsligHandleevne") val rettsligHandleevne: List<PdlPipRettsligHandleevne>,
)

data class PdlPipAdressebeskyttelse(@JsonProperty("gradering") val gradering: AdressebeskyttelseGradering?)

data class PdlPipFødselsdato(@JsonProperty("foedselsdato") val fødselsdato: LocalDate)

data class PdlPipDødsfall(@JsonProperty("doedsdato") val dødsdato: LocalDate)

data class PdlPipFamilierelasjon(
    @JsonProperty("relatertPersonsIdent") val relatertPersonsIdent: Fødselsnummer?,
    @JsonProperty("relatertPersonsRolle") val relatertPersonsRolle: String?,
    @JsonProperty("minRolleForPerson") val minRolleForPerson: String?,
)

data class PdlPipRettsligHandleevne(@JsonProperty("rettsligHandleevneomfang") val omfang: String?)
