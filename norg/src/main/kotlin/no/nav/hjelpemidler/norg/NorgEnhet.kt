package no.nav.hjelpemidler.norg

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.hjelpemidler.domain.enhet.AbstractEnhet
import no.nav.hjelpemidler.domain.enhet.Enhet
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.domain.enhet.TilknyttetEnhet
import no.nav.hjelpemidler.domain.kodeverk.Kodeverk

class NorgEnhet(
    @JsonAlias("enhetNr")
    override val nummer: Enhetsnummer,
    override val navn: String,
    val status: Kodeverk<Status>,
    val type: Kodeverk<Type>,
    @JsonAlias("oppgavebehandler")
    private val isOppgavebehandler: Boolean,
) : AbstractEnhet<Enhetsnummer>(), TilknyttetEnhet {
    override val enhet: Enhet
        @JsonIgnore
        get() = Enhet(nummer, navn)

    override fun toString(): String = "'$navn' (nummer: $nummer, status: $status, type: $type)"

    enum class Status : Kodeverk<Status> {
        @JsonAlias("Under etablering")
        UNDER_ETABLERING,

        @JsonAlias("Aktiv")
        AKTIV,

        @JsonAlias("Under avvikling")
        UNDER_AVVIKLING,

        @JsonAlias("Nedlagt")
        NEDLAGT,
    }

    enum class Type : Kodeverk<Type> {
        HMS,
        IT,
        KO,
    }
}
