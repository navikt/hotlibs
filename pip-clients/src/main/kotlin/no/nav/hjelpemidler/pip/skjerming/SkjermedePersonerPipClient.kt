package no.nav.hjelpemidler.pip.skjerming

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import no.nav.hjelpemidler.configuration.EnvironmentVariable
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.http.correlationId
import no.nav.hjelpemidler.http.createHttpClient
import no.nav.hjelpemidler.http.openid.asApplication
import no.nav.hjelpemidler.http.openid.entraID
import java.io.Closeable

private val log = KotlinLogging.logger {}

class SkjermedePersonerPipClient(
    engine: HttpClientEngine,
    private val baseUrl: String = SKJERMEDE_PERSONER_PIP_API_BASE_URL,
    private val target: String = SKJERMEDE_PERSONER_PIP_API_TARGET,
) : Closeable {
    private val client = createHttpClient(engine) {
        expectSuccess = true
        entraID(target)
        defaultRequest {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)
            correlationId()
            asApplication()
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
        if (fnr.isEmpty()) return emptyMap()
        if (fnr.size == 1) return fnr.associateWith { hentErSkjermetPerson(it) }
        val url = "$baseUrl/skjermetBulk"
        log.debug { "Henter informasjon om personer er skjermet fra url: '$url', antall: ${fnr.size}" }
        return client
            .post(url) { setBody(ErSkjermedePersonerRequest(fnr)) }
            .body()
    }

    override fun close() = client.close()

    companion object {
        val SKJERMEDE_PERSONER_PIP_API_BASE_URL by EnvironmentVariable
        val SKJERMEDE_PERSONER_PIP_API_TARGET by EnvironmentVariable
    }
}
