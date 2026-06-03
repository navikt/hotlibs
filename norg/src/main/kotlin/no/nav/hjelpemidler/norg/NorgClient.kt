package no.nav.hjelpemidler.norg

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import no.nav.hjelpemidler.configuration.Configuration
import no.nav.hjelpemidler.domain.enhet.Enhet
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.domain.enhet.TilknyttetEnhet
import no.nav.hjelpemidler.http.createHttpClient

private val log = KotlinLogging.logger {}

class NorgClient(
    engine: HttpClientEngine = CIO.create(),
    private val baseUrl: String = Configuration["NORG_API_URL"] ?: "https://norg2.prod-fss-pub.nais.io/norg2/api/v1",
) {
    private val client = createHttpClient(engine) {
        expectSuccess = true
        defaultRequest {
            accept(ContentType.Application.Json)
        }
    }

    suspend fun hentArbeidsfordeling(geografiskOmråde: String): List<Enhet> {
        val url = "$baseUrl/arbeidsfordeling/enheter/bestmatch"
        log.debug { "Henter arbeidsfordeling med url: '$url', geografiskOmråde: $geografiskOmråde" }
        return client
            .post(url) {
                contentType(ContentType.Application.Json)
                setBody(NorgArbeidsfordelingRequest(geografiskOmråde))
            }
            .body()
    }

    suspend fun hentArbeidsfordelinger(geografiskeOmråder: Set<String>): Map<String, List<Enhet>> = coroutineScope {
        geografiskeOmråder
            .map { async { it to hentArbeidsfordeling(it) } }
            .awaitAll()
            .toMap()
    }

    suspend fun hentNorgEnhet(id: Enhetsnummer): NorgEnhet {
        val url = "$baseUrl/enhet/$id"
        log.debug { "Henter enhet med url: '$url', enhetsnummer: '$id'" }
        return client.get(url).body()
    }

    suspend fun hentNorgEnhet(id: String): NorgEnhet = hentNorgEnhet(Enhetsnummer(id))

    suspend fun hentNorgEnhet(tilknyttetEnhet: TilknyttetEnhet): NorgEnhet = hentNorgEnhet(tilknyttetEnhet.enhetsnummer)

    suspend fun hentNorgEnheter(ider: Set<Enhetsnummer>): Map<Enhetsnummer, NorgEnhet> = coroutineScope {
        ider
            .map { async { hentNorgEnhet(it) } }
            .awaitAll()
            .associateByTo(sortedMapOf(), NorgEnhet::enhetsnummer)
    }

    suspend fun hentEnhet(id: Enhetsnummer): Enhet = hentNorgEnhet(id).enhet

    suspend fun hentEnhet(id: String): Enhet = hentEnhet(Enhetsnummer(id))
}
