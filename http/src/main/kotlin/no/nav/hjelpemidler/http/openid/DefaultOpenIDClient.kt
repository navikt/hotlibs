package no.nav.hjelpemidler.http.openid

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.submitForm
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import io.ktor.http.parameters
import no.nav.hjelpemidler.http.createHttpClient

private val log = KotlinLogging.logger {}

internal open class DefaultOpenIDClient(
    private val configuration: OpenIDClientConfiguration,
    engine: HttpClientEngine = CIO.create(),
) : OpenIDClient {
    private val client: HttpClient = createHttpClient(engine) {
        expectSuccess = false
    }

    override suspend fun grant(scope: String): TokenSet = execute {
        grantType(GrantType.CLIENT_CREDENTIALS)
        scope(scope)
    }

    override suspend fun grant(scope: String, onBehalfOf: String): TokenSet = execute {
        grantType(GrantType.JWT_BEARER)
        scope(scope)
        assertion(onBehalfOf)
        requestedTokenUse("on_behalf_of")
    }

    private suspend fun execute(builder: ParametersBuilder.() -> Unit): TokenSet {
        val formParameters = parameters {
            clientId(configuration.clientId)
            clientSecret(checkNotNull(configuration.clientSecret) {
                "Mangler verdi for 'clientSecret'"
            })
            builder()
        }
        return execute(formParameters)
    }

    protected open suspend fun execute(formParameters: Parameters): TokenSet {
        log.debug { "Henter token, scope: '${formParameters.scope}', grantType: '${formParameters.grantType}'" }
        val response = client.submitForm(url = configuration.tokenEndpoint, formParameters = formParameters)
        return when (response.status) {
            HttpStatusCode.OK -> {
                val tokenSet = response.body<TokenSet>()
                log.debug { "Hentet token, expiresAt: ${tokenSet.expiresAt}" }
                tokenSet
            }

            HttpStatusCode.BadRequest -> openIDError(formParameters, "Ugyldig forespørsel", response)
            else -> openIDError(formParameters, "Noe gikk galt", response)
        }
    }
}
