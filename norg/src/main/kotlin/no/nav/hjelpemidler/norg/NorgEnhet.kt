package no.nav.hjelpemidler.saksbehandling.integrations.norg

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.hjelpemidler.domain.enhet.Enhet
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.domain.enhet.TilknyttetEnhet
import no.nav.hjelpemidler.domain.kodeverk.Kodeverk

class NorgEnhet(
    @JsonAlias("enhetNr")
    val nummer: Enhetsnummer,
    val navn: String,
    val status: Kodeverk<Status>,
    val type: Kodeverk<Type>,
) : TilknyttetEnhet, Comparable<NorgEnhet> {
    override val enhet: Enhet
        @JsonIgnore
        get() = Enhet(nummer, navn)

    val isStatusAktiv: Boolean
        @JsonIgnore
        get() = status == Status.AKTIV

    val isTypeHjelpemiddelsentral: Boolean
        @JsonIgnore
        get() = type == Type.HMS

    val isTypeIT: Boolean
        @JsonIgnore
        get() = type == Type.IT

    val isVikafossen: Boolean
        @JsonIgnore
        get() = enhet == Enhet.NAV_VIKAFOSSEN

    /**
     * Er enheten en aktiv hjelpemiddelsentral (eller Nav Vikafossen som også behandler saker i Hotsak)?
     * Inkluderer IT-avdelingen for test.
     */
    val isOppgavebehandler: Boolean
        @JsonIgnore
        get() = isStatusAktiv && (isTypeHjelpemiddelsentral || isTypeIT || isVikafossen)

    override fun compareTo(other: NorgEnhet): Int = nummer.compareTo(other.nummer)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as NorgEnhet
        return nummer == other.nummer
    }

    override fun hashCode(): Int = nummer.hashCode()

    override fun toString(): String = "${enhet.navn} (nummer: '${enhet.nummer}', status: $status, type: $type)"

    enum class Status : Kodeverk<Status> {
        UNDER_ETABLERING,
        AKTIV,
        UNDER_AVVIKLING,
        NEDLAGT,
    }

    enum class Type : Kodeverk<Type> {
        HMS,
        IT,
    }
}
