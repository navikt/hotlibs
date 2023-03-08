package no.nav.hjelpemidler.http.openid

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import mu.KotlinLogging
import no.nav.hjelpemidler.http.createHttpClient

private val log = KotlinLogging.logger {}

internal class DefaultOpenIDClient(
    private val configuration: OpenIDConfiguration,
    engine: HttpClientEngine = CIO.create(),
) : OpenIDClient {
    private val client: HttpClient = createHttpClient(engine = engine) {
        expectSuccess = false
    }

    override suspend fun grant(builder: ParametersBuilder.() -> Unit): TokenSet {
        val formParameters = Parameters.build {
            clientId(configuration.clientId)
            clientSecret(checkNotNull(configuration.clientSecret) {
                "Mangler verdi for 'clientSecret'"
            })
            builder()
        }
        log.debug { "Henter token, scope: ${formParameters.scope}" }
        val response = client
            .submitForm(url = configuration.tokenEndpoint, formParameters = formParameters)
        return when (response.status) {
            HttpStatusCode.OK -> {
                val tokenSet = response.body<TokenSet>()
                log.debug {
                    "Hentet token, scope: ${formParameters.scope}, expiresAt: ${tokenSet.expiresAt}"
                }
                tokenSet
            }

            HttpStatusCode.BadRequest -> openIDError("Ugyldig forespørsel, status: '${response.status}', body: '${response.body<Map<String, Any?>>()}'")
            else -> openIDError("Noe gikk galt, status: '${response.status}', body: '${response.bodyAsText()}'")
        }
    }
}
