package no.nav.hjelpemidler.norg

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.domain.geografi.GeografiskOmråde
import no.nav.hjelpemidler.domain.kodeverk.EksterntKodeverk
import no.nav.hjelpemidler.domain.kodeverk.JsonSerializeEksterntKodeverk
import no.nav.hjelpemidler.domain.kodeverk.Tema
import no.nav.hjelpemidler.domain.person.AdressebeskyttelseGradering

@JsonInclude(JsonInclude.Include.NON_NULL)
data class NorgArbeidsfordelingRequest(
    @JsonSerializeEksterntKodeverk
    val oppgavetype: EksterntKodeverk? = null,
    @JsonSerializeEksterntKodeverk
    val behandlingstema: EksterntKodeverk? = null,
    @JsonSerializeEksterntKodeverk
    val behandlingstype: EksterntKodeverk? = null,
    @JsonProperty("geografiskOmraade")
    val geografiskOmråde: String? = null,
    val diskresjonskode: Diskresjonskode? = null,
    @JsonProperty("skjermet")
    val isSkjermet: Boolean = false,
) {
    val tema: Tema = Tema.HJE
    val temagruppe: String = "HJLPM" // fixme -> trenger vi denne, hva gjør den egentlig?

    // @JsonProperty("enhetNummer")
    // val enhet: Enhetsnummer? = null

    @JsonIgnore
    constructor(
        oppgavetype: EksterntKodeverk? = null,
        behandlingstema: EksterntKodeverk? = null,
        behandlingstype: EksterntKodeverk? = null,
        geografiskOmråde: GeografiskOmråde? = null,
        adressebeskyttelseGradering: AdressebeskyttelseGradering? = null,
        isSkjermet: Boolean = false,
    ) : this(
        oppgavetype = oppgavetype,
        behandlingstema = behandlingstema,
        behandlingstype = behandlingstype,
        geografiskOmråde = geografiskOmråde?.id,
        diskresjonskode = when (adressebeskyttelseGradering) {
            AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND,
            AdressebeskyttelseGradering.STRENGT_FORTROLIG,
                -> Diskresjonskode.SPSF

            AdressebeskyttelseGradering.FORTROLIG,
                -> Diskresjonskode.SPFO

            else -> null
        },
        isSkjermet = isSkjermet
    )

    override fun toString(): String =
        "oppgavetype: $oppgavetype, behandlingstema: $behandlingstema, behandlingstype: $behandlingstype, geografiskOmråde: $geografiskOmråde"

    enum class Diskresjonskode { SPSF, SPFO }
}
