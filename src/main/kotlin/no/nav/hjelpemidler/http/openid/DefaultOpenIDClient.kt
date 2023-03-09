package no.nav.hjelpemidler.http.openid

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.submitForm
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import mu.KotlinLogging
import mu.withLoggingContext
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
        return withLoggingContext(
            "grantType" to formParameters.grantType,
            "scope" to formParameters.scope,
        ) {
            log.debug { "Henter token" }
            val response = client
                .submitForm(url = configuration.tokenEndpoint, formParameters = formParameters)
            when (response.status) {
                HttpStatusCode.OK -> {
                    val tokenSet = response.body<TokenSet>()
                    log.debug {
                        "Hentet token, expiresAt: ${tokenSet.expiresAt}"
                    }
                    tokenSet
                }

                HttpStatusCode.BadRequest -> openIDError(formParameters, "Ugyldig forespørsel", response)
                else -> openIDError(formParameters, "Noe gikk galt", response)
            }
        }
    }
}
