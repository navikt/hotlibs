package no.nav.hjelpemidler.populasjonskontroll.tilgangsmaskinen

import io.ktor.http.HttpStatusCode
import no.nav.hjelpemidler.domain.person.PersonId
import no.nav.hjelpemidler.domain.tilgang.NavIdent
import no.nav.hjelpemidler.populasjonskontroll.tilgangsmaskinen.TilgangsmaskinenResponse.Avvist
import java.net.URI

object TilgangsmaskinenResponses {
    val GODKJENT: TilgangsmaskinenResponse = TilgangsmaskinenResponse.Godkjent

    fun avvist(
        brukerIdent: PersonId,
        navIdent: NavIdent,
        title: Avvist.Kode = Avvist.Kode.AVVIST_STRENGT_FORTROLIG_ADRESSE,
    ) = Avvist(
        type = URI.create("https://confluence.adeo.no/display/TM/Tilgangsmaskin+API+og+regelsett"),
        title = title.name,
        status = HttpStatusCode.Forbidden,
        detail = null,
        instance = null,
        brukerIdent = brukerIdent,
        navIdent = navIdent,
        begrunnelse = title.name,
        traceId = "TEST",
        kanOverstyres = false,
    )
}
