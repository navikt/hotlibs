package no.nav.hjelpemidler.pip

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.hjelpemidler.domain.person.AdressebeskyttelseGradering
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.NPID
import no.nav.hjelpemidler.domain.person.PersonId
import no.nav.hjelpemidler.pip.pdl.PdlPipApiClient
import no.nav.hjelpemidler.pip.pdl.PipPersonResponse
import no.nav.hjelpemidler.pip.skjerming.SkjermedePersonerPipClient
import org.slf4j.LoggerFactory
import java.time.LocalDate

val log = LoggerFactory.getLogger("PipService")

class PipService(
    private val pdlPipApiClient: PdlPipApiClient,
    private val skjermedePersonerPipClient: SkjermedePersonerPipClient,
) {
    suspend fun hentPerson(id: PersonId): PipResponse {
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
                    log.warn("Kan ikke utlede skjerming for person uten fødselsnummer")
                    false
                } else {
                    skjermedePersonerPipClient.hentErSkjermetPerson(fnr)
                }
                pipPersonResponse to isSkjermet
            }
        }
        return PipResponse(pipPersonResponse, isSkjermet)
    }

    suspend fun hentPersoner(ider: Set<PersonId>): Map<PersonId, PipResponse> {
        TODO()
    }
}

data class PipResponse(
    val aktørId: AktørId,
    val fnr: Fødselsnummer?,
    val gjeldendeIdenter: Set<PersonId>,
    val fødselsdato: LocalDate?,
    val dødsdato: LocalDate?,
    val gradering: AdressebeskyttelseGradering,
    val isSkjermet: Boolean,
) {
    constructor(pipPersonResponse: PipPersonResponse, isSkjermet: Boolean) : this(
        aktørId = pipPersonResponse.aktørId,
        fnr = pipPersonResponse.fnr,
        gjeldendeIdenter = pipPersonResponse.gjeldendeIdenter,
        fødselsdato = pipPersonResponse.person.fødselsdato.firstOrNull()?.fødselsdato,
        dødsdato = pipPersonResponse.person.dødsfall.firstOrNull()?.dødsdato,
        gradering = pipPersonResponse.gradering,
        isSkjermet = isSkjermet,
    )
}
