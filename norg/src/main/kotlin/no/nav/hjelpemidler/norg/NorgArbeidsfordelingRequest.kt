package no.nav.hjelpemidler.norg

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import no.nav.hjelpemidler.domain.geografi.GeografiskOmråde
import no.nav.hjelpemidler.domain.kodeverk.Tema
import no.nav.hjelpemidler.domain.person.AdressebeskyttelseGradering
import no.nav.hjelpemidler.norg.NorgArbeidsfordelingRequest.Diskresjonskode

data class NorgArbeidsfordelingRequest(
    val oppgavetype: String? = null,
    val behandlingstema: String? = null,
    val behandlingstype: String? = null,
    @JsonProperty("geografiskOmraade")
    val geografiskOmråde: String? = null,
    val diskresjonskode: Diskresjonskode? = null,
    val skjermet: Boolean = false,
) {
    val tema = Tema.HJE
    val temagruppe = "HJLPM" // fixme -> trenger vi denne, hva gjør den egentlig?

    // @JsonProperty("enhetNummer")
    // val enhet: Enhetsnummer? = null

    @JsonIgnore
    constructor(
        oppgavetype: String? = null,
        behandlingstema: String? = null,
        behandlingstype: String? = null,
        geografiskOmråde: GeografiskOmråde? = null,
        gradering: AdressebeskyttelseGradering? = null,
        skjermet: Boolean = false,
    ) : this(
        oppgavetype = oppgavetype,
        behandlingstema = behandlingstema,
        behandlingstype = behandlingstype,
        geografiskOmråde = geografiskOmråde?.id,
        diskresjonskode = gradering?.diskresjonskode,
        skjermet = skjermet
    )

    override fun toString(): String =
        "oppgavetype: $oppgavetype, behandlingstema: $behandlingstema, behandlingstype: $behandlingstype, geografiskOmråde: $geografiskOmråde"

    enum class Diskresjonskode { SPSF, SPFO }
}

val AdressebeskyttelseGradering.diskresjonskode: Diskresjonskode?
    get() = when (this) {
        AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND,
        AdressebeskyttelseGradering.STRENGT_FORTROLIG,
            -> Diskresjonskode.SPSF

        AdressebeskyttelseGradering.FORTROLIG,
            -> Diskresjonskode.SPFO

        else -> null
    }
