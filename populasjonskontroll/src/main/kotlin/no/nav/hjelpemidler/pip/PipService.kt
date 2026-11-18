package no.nav.hjelpemidler.pip

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.hjelpemidler.collections.filterIsInstanceToSet
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.NPID
import no.nav.hjelpemidler.domain.person.PersonId
import no.nav.hjelpemidler.pip.pdl.PdlPipApiClient
import no.nav.hjelpemidler.pip.pdl.PdlPipPersonResponse
import no.nav.hjelpemidler.pip.skjerming.SkjermedePersonerPipClient

private val log = KotlinLogging.logger {}

class PipService(
    private val pdlPipApiClient: PdlPipApiClient,
    private val skjermedePersonerPipClient: SkjermedePersonerPipClient,
) {
    suspend fun hentPerson(id: PersonId): PipPerson {
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
        return PipPerson(pipPersonResponse, isSkjermet)
    }

    suspend fun hentPersoner(ider: Set<PersonId>): Map<PersonId, PipPerson> {
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
            PipPerson(it.value, isSkjermetById.getOrDefault(it.fnr, false))
        }
    }
}

private val Map.Entry<PersonId, PdlPipPersonResponse>.fnr: Fødselsnummer? get() = key as? Fødselsnummer ?: value.fnr
