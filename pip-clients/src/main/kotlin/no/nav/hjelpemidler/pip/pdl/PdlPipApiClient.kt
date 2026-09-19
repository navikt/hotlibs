package no.nav.hjelpemidler.pip.pdl

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
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
import no.nav.hjelpemidler.http.createHttpClient
import no.nav.hjelpemidler.http.openid.TokenSetProvider
import no.nav.hjelpemidler.http.openid.openID

private val log = KotlinLogging.logger {}

internal val PDL_PIP_API_URL by EnvironmentVariable

class PdlPipApiClient(
    engine: HttpClientEngine = CIO.create(),
    tokenSetProvider: TokenSetProvider,
    private val baseUrl: String = PDL_PIP_API_URL,
) {
    private val client = createHttpClient(engine) {
        expectSuccess = true
        openID(tokenSetProvider)
        defaultRequest {
            accept(ContentType.Application.Json)
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
}
