package no.nav.hjelpemidler.populasjonskontroll.tilgangsmaskinen

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.accept
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.resources.Resource
import no.nav.hjelpemidler.configuration.EnvironmentVariable
import no.nav.hjelpemidler.domain.person.PersonId
import no.nav.hjelpemidler.http.ProblemDetailsException
import no.nav.hjelpemidler.http.correlationId
import no.nav.hjelpemidler.http.createHttpClient
import no.nav.hjelpemidler.http.openid.asPrincipal
import no.nav.hjelpemidler.http.openid.entraId
import no.nav.hjelpemidler.http.problemDetails
import no.nav.hjelpemidler.security.AutentisertAnsatt
import java.io.Closeable

private val log = KotlinLogging.logger {}

class TilgangsmaskinenClient(
    engine: HttpClientEngine,
    private val baseUrl: String = TILGANGSMASKINEN_BASE_URL,
    private val target: String = TILGANGSMASKINEN_TARGET,
) : Tilgangsmaskinen, Closeable {
    private val client = createHttpClient(engine) {
        expectSuccess = false
        entraId(target)
        install(Resources)
        defaultRequest {
            url("$baseUrl/api/v1/")
            accept(ContentType.Application.Json)
            correlationId()
        }
    }

    override suspend fun evaluerKjernereglerForAnsatt(
        ansatt: AutentisertAnsatt,
        personId: PersonId,
    ): TilgangsmaskinenResponse {
        return try {
            val response = client.post(KjerneResource()) {
                asPrincipal(ansatt)
                contentType(ContentType.Application.Json)
                setBody(personId)
            }
            when {
                response.status.isSuccess() -> TilgangsmaskinenResponse.Godkjent
                response.status == HttpStatusCode.Forbidden -> response.body<TilgangsmaskinenResponse.Avvist>()
                else -> response.error()
            }
        } catch (e: Exception) {
            log.error(e) { "Feil ved evaluering av kjerneregler for ansatt" }
            throw e
        }
    }

    override suspend fun evaluerKjernereglerForAnsatt(
        ansatt: AutentisertAnsatt,
        personIder: Set<PersonId>,
    ): TilgangsmaskinenBulkResponse {
        if (personIder.isEmpty()) return TilgangsmaskinenBulkResponse(ansatt.id, emptyList())
        return try {
            val response = client.post(BulkResource.Obo.RegelType(Tilgangsmaskinen.RegelType.KJERNE_REGELTYPE)) {
                asPrincipal(ansatt)
                contentType(ContentType.Application.Json)
                setBody(personIder)
            }
            if (response.status.isSuccess()) {
                response.body<TilgangsmaskinenBulkResponse>()
            } else {
                response.error()
            }
        } catch (e: Exception) {
            log.error(e) { "Feil ved evaluering av kjerneregler for ansatt" }
            throw e
        }
    }

    override fun close() {
        client.close()
    }

    @Resource("/kjerne")
    internal class KjerneResource

    @Resource("/bulk")
    internal class BulkResource {
        @Resource("/obo")
        class Obo(val parent: BulkResource = BulkResource()) {
            @Resource("/{regelType}")
            class RegelType(
                val regelType: Tilgangsmaskinen.RegelType = Tilgangsmaskinen.RegelType.KJERNE_REGELTYPE,
                val parent: Obo = Obo(),
            )
        }
    }

    companion object {
        val TILGANGSMASKINEN_BASE_URL by EnvironmentVariable
        val TILGANGSMASKINEN_TARGET by EnvironmentVariable
    }
}

private suspend fun HttpResponse.error(): Nothing {
    val details = problemDetails() ?: error("Uventet svar fra Tilgangsmaskinen, status: $status")
    throw ProblemDetailsException(details)
}
