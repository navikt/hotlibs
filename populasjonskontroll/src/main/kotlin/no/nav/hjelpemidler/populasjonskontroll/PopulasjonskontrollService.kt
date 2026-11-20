package no.nav.hjelpemidler.populasjonskontroll

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.hjelpemidler.collections.filterIsInstanceToSet
import no.nav.hjelpemidler.domain.enhet.Enhet
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.NPID
import no.nav.hjelpemidler.domain.person.PersonId
import no.nav.hjelpemidler.logging.teamWarn
import no.nav.hjelpemidler.norg.NorgArbeidsfordelingRequest
import no.nav.hjelpemidler.norg.NorgClient
import no.nav.hjelpemidler.populasjonskontroll.pdl.PdlPipApiClient
import no.nav.hjelpemidler.populasjonskontroll.pdl.PdlPipPersonResponse
import no.nav.hjelpemidler.populasjonskontroll.skjerming.SkjermedePersonerPipClient
import no.nav.hjelpemidler.populasjonskontroll.tilgangsmaskinen.TilgangsmaskinenBulkResponse
import no.nav.hjelpemidler.populasjonskontroll.tilgangsmaskinen.TilgangsmaskinenClient
import no.nav.hjelpemidler.populasjonskontroll.tilgangsmaskinen.TilgangsmaskinenResponse
import no.nav.hjelpemidler.security.AutentisertAnsatt
import no.nav.hjelpemidler.security.AutentisertApplikasjon
import no.nav.hjelpemidler.security.AutentisertInnbygger
import no.nav.hjelpemidler.security.AuthenticatedPrincipal

private val log = KotlinLogging.logger {}

class PopulasjonskontrollService(
    private val norgClient: NorgClient,
    private val pdlPipApiClient: PdlPipApiClient,
    private val skjermedePersonerPipClient: SkjermedePersonerPipClient,
    private val tilgangsmaskinenClient: TilgangsmaskinenClient,
) {
    suspend fun hentPerson(id: PersonId): PopulasjonskontrollPerson {
        val (pipPersonResponse, isSkjermet) = when (id) {
            is Fødselsnummer -> coroutineScope {
                val pipPersonResponse = async { pdlPipApiClient.hentPerson(id) }
                val isSkjermet = async { skjermedePersonerPipClient.hentErSkjermetPerson(id) }
                pipPersonResponse.await() to isSkjermet.await()
            }

            // skjermedePersonerPipClient støtter bare fødselsnummer, derfor kaller vi pdlPipApiClient først
            is AktørId, is NPID -> {
                val pipPersonResponse = pdlPipApiClient.hentPerson(id)
                val fnr = pipPersonResponse.fnr
                val isSkjermet = if (fnr == null) {
                    log.warn { "Kan ikke utlede skjerming for person uten fødselsnummer" }
                    false
                } else {
                    skjermedePersonerPipClient.hentErSkjermetPerson(fnr)
                }
                pipPersonResponse to isSkjermet
            }
        }
        return PopulasjonskontrollPerson(pipPersonResponse = pipPersonResponse, isSkjermet = isSkjermet)
    }

    suspend fun hentPersoner(ider: Set<PersonId>): Map<PersonId, PopulasjonskontrollPerson> {
        if (ider.isEmpty()) return emptyMap()
        if (ider.size == 1) return ider.associateWith { hentPerson(it) }

        val fnr = ider.filterIsInstanceToSet<Fødselsnummer>()
        val (pipPersonResponseById, isSkjermetById) = if (fnr.containsAll(ider)) {
            coroutineScope {
                val pipPersonResponseById = async { pdlPipApiClient.hentPersoner(fnr) }
                val isSkjermetById = async { skjermedePersonerPipClient.hentErSkjermedePersoner(fnr) }
                pipPersonResponseById.await() to isSkjermetById.await()
            }
        } else {
            val pipPersonResponseById = pdlPipApiClient.hentPersoner(ider)
            val isSkjermetById = skjermedePersonerPipClient.hentErSkjermedePersoner(
                pipPersonResponseById.mapNotNullTo(mutableSetOf()) { it.fnr },
            )
            pipPersonResponseById to isSkjermetById
        }

        return pipPersonResponseById.mapValues {
            PopulasjonskontrollPerson(
                pipPersonResponse = it.value,
                isSkjermet = isSkjermetById.getOrDefault(it.fnr, false),
            )
        }
    }

    suspend fun hentArbeidsfordeling(id: PersonId): Pair<PopulasjonskontrollPerson, Enhet> {
        val person = hentPerson(id)
        val enheter = norgClient.hentArbeidsfordeling(
            NorgArbeidsfordelingRequest(
                geografiskOmråde = person.geografiskOmråde,
                adressebeskyttelseGradering = person.adressebeskyttelseGradering,
                isSkjermet = person.isSkjermet,
            )
        )

        var tilknyttetEnhet = enheter.firstOrNull() ?: error("Kunne ikke utlede arbeidsfordeling for person")
        if (person.adressebeskyttelseGradering.isStrengtFortrolig && tilknyttetEnhet != Enhet.NAV_VIKAFOSSEN) {
            log.teamWarn { "Uventet arbeidsfordeling for person med gradering, enhet: $tilknyttetEnhet" }
            tilknyttetEnhet = Enhet.NAV_VIKAFOSSEN
        }

        return person to tilknyttetEnhet
    }

    suspend fun evaluerKjerneregler(
        principal: AuthenticatedPrincipal,
        personId: PersonId,
    ): TilgangsmaskinenResponse = when (principal) {
        is AutentisertApplikasjon -> TilgangsmaskinenResponse.Godkjent
        is AutentisertAnsatt -> tilgangsmaskinenClient.evaluerKjernereglerForAnsatt(principal, personId)
        is AutentisertInnbygger -> if (principal.id == personId) {
            TilgangsmaskinenResponse.Godkjent
        } else {
            TilgangsmaskinenResponse.AVVIST
        }
    }

    suspend fun evaluerKjerneregler(
        principal: AuthenticatedPrincipal,
        personIder: Set<PersonId>,
    ): TilgangsmaskinenBulkResponse = when (principal) {
        is AutentisertApplikasjon -> TODO()
        is AutentisertAnsatt -> tilgangsmaskinenClient.evaluerKjernereglerForAnsatt(principal, personIder)
        is AutentisertInnbygger -> TODO()
    }
}

private val Map.Entry<PersonId, PdlPipPersonResponse>.fnr: Fødselsnummer? get() = key as? Fødselsnummer ?: value.fnr
