package no.nav.hjelpemidler.norg

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.hjelpemidler.domain.enhet.Enhet
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.domain.enhet.TilknyttetEnhet
import no.nav.hjelpemidler.domain.kodeverk.Kodeverk
import no.nav.hjelpemidler.domain.kodeverk.Tema
import java.time.LocalDate

data class NorgArbeidsfordeling(
    val diskresjonskode: String = ANY,
    val oppgavetype: String = ANY,
    val behandlingstype: String = ANY,
    val behandlingstema: String = ANY,
    val tema: Kodeverk<Tema>,
    val temagruppe: String = ANY,
    @JsonAlias("geografiskOmraade")
    val geografiskOmråde: String,
    private val enhetNr: Enhetsnummer,
    private val enhetNavn: String,
    val gyldigFra: LocalDate,
    val gyldigTil: LocalDate? = null,
) : TilknyttetEnhet {
    override val enhet: Enhet
        @JsonIgnore
        get() = Enhet(nummer = enhetNr, navn = enhetNavn)

    companion object {
        const val ANY = "ANY"
    }
}
