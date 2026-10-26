package no.nav.hjelpemidler.pip.pdl

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import no.nav.hjelpemidler.configuration.EnvironmentVariable
import no.nav.hjelpemidler.domain.person.PersonId
import no.nav.hjelpemidler.http.correlationId
import no.nav.hjelpemidler.http.createHttpClient
import no.nav.hjelpemidler.http.openid.asApplication
import no.nav.hjelpemidler.http.openid.entraId
import java.io.Closeable

private val log = KotlinLogging.logger {}

class PdlPipApiClient(
    engine: HttpClientEngine,
    private val baseUrl: String = PDL_PIP_API_BASE_URL,
    private val target: String = PDL_PIP_API_TARGET,
) : Closeable {
    private val client = createHttpClient(engine) {
        expectSuccess = true
        entraId(target)
        defaultRequest {
            accept(ContentType.Application.Json)
            correlationId()
            asApplication()
        }
    }

    suspend fun hentPerson(id: PersonId): PdlPipPersonResponse {
        val url = "$baseUrl/person"
        log.debug { "Henter person fra pdl-pip-api med url: '$url'" }
        return client
            .get(url) { header("ident", id.value) }
            .body()
    }

    suspend fun hentPersoner(ider: Set<PersonId>): Map<PersonId, PdlPipPersonResponse> {
        if (ider.isEmpty()) return emptyMap()
        if (ider.size == 1) return ider.associateWith { hentPerson(it) }
        val url = "$baseUrl/personBolk"
        log.debug { "Henter personer fra pdl-pip-api med url: '$url', antall: ${ider.size}" }
        return client
            .post(url) {
                contentType(ContentType.Application.Json)
                setBody(ider)
            }
            .body()
    }

    override fun close() = client.close()

    companion object {
        val PDL_PIP_API_BASE_URL by EnvironmentVariable
        val PDL_PIP_API_TARGET by EnvironmentVariable
    }
}
