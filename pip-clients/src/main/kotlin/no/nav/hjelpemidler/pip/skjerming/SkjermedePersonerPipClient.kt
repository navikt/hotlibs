package no.nav.hjelpemidler.pip.skjerming

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import no.nav.hjelpemidler.configuration.EnvironmentVariable
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.http.createHttpClient
import no.nav.hjelpemidler.http.openid.TokenSetProvider
import no.nav.hjelpemidler.http.openid.openID

private val log = KotlinLogging.logger {}

internal val SKJERMEDE_PERSONER_PIP_API_URL by EnvironmentVariable

class SkjermedePersonerPipClient(
    engine: HttpClientEngine = CIO.create(),
    tokenSetProvider: TokenSetProvider,
    private val baseUrl: String = SKJERMEDE_PERSONER_PIP_API_URL,
) {
    private val client = createHttpClient(engine) {
        expectSuccess = true
        openID(tokenSetProvider)
        defaultRequest {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun hentErSkjermetPerson(fnr: Fødselsnummer): Boolean {
        val url = "$baseUrl/skjermet"
        log.debug { "Henter informasjon om person er skjermet fra url: '$url'" }
        return client
            .post(url) { setBody(ErSkjermetPersonRequest(fnr)) }
            .body<Boolean>()
    }

    suspend fun hentErSkjermedePersoner(fnr: Set<Fødselsnummer>): Map<Fødselsnummer, Boolean> {
        val url = "$baseUrl/skjermetBulk"
        log.debug { "Henter informasjon om personer er skjermet fra url: '$url', antall: ${fnr.size}" }
        if (fnr.isEmpty()) return emptyMap()
        return client
            .post(url) { setBody(ErSkjermedePersonerRequest(fnr)) }
            .body()
    }
}
