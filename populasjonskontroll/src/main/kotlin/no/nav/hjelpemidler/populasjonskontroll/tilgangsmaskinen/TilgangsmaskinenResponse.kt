package no.nav.hjelpemidler.populasjonskontroll.tilgangsmaskinen

import io.ktor.http.HttpStatusCode
import no.nav.hjelpemidler.core.enumValueOfOrNull
import no.nav.hjelpemidler.domain.person.PersonId
import no.nav.hjelpemidler.domain.tilgang.NavIdent
import no.nav.hjelpemidler.http.ProblemDetails
import java.net.URI

interface Tilgangsmaskinen {
    enum class RegelType { KJERNE_REGELTYPE, KOMPLETT_REGELTYPE, OVERSTYRBAR_REGELTYPE }
}

sealed interface TilgangsmaskinenResponse {
    data object Godkjent : TilgangsmaskinenResponse

    data class Avvist(
        override val type: URI?,
        override val title: String?,
        override val status: HttpStatusCode,
        override val detail: String?,
        override val instance: URI?,

        val brukerIdent: PersonId?,
        val navIdent: NavIdent?,
        val begrunnelse: String?,
        val traceId: String?,
        val kanOverstyres: Boolean?,
    ) : TilgangsmaskinenResponse, ProblemDetails {
        val kode: Kode? get() = title?.let(::enumValueOfOrNull)

        enum class Kode {
            AVVIST_AVDØD,
            AVVIST_FORTROLIG_ADRESSE,
            AVVIST_GEOGRAFISK,
            AVVIST_HABILITET,
            AVVIST_PERSON_UTLAND,
            AVVIST_SKJERMING,
            AVVIST_STRENGT_FORTROLIG_ADRESSE,
            AVVIST_STRENGT_FORTROLIG_UTLAND,
            AVVIST_UKJENT_BOSTED,
            AVVIST_VERGEMÅL,
        }
    }

    companion object {
        val AVVIST = Avvist(
            type = null,
            title = "hotlibs",
            status = HttpStatusCode.Forbidden,
            detail = null,
            instance = null,
            brukerIdent = null,
            navIdent = null,
            begrunnelse = null,
            traceId = null,
            kanOverstyres = null,
        )
    }
}
