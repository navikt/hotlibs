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
import no.nav.hjelpemidler.http.createHttpClient

class OpenIDClient(
    private val configuration: OpenIDConfigurationProvider,
    engine: HttpClientEngine = CIO.create(),
) {
    private val client: HttpClient = createHttpClient(engine = engine) {
        expectSuccess = false
    }

    fun ParametersBuilder.grantType(value: String) = append("grant_type", value)
    fun ParametersBuilder.clientId(value: String) = append("client_id", value)
    fun ParametersBuilder.clientSecret(value: String) = append("client_secret", value)
    fun ParametersBuilder.assertion(value: String) = append("assertion", value)
    fun ParametersBuilder.scope(value: String) = append("scope", value)
    fun ParametersBuilder.requestedTokenUse(value: String) = append("requested_token_use", value)

    suspend fun grant(builder: ParametersBuilder.() -> Unit): TokenSet {
        val response = client
            .submitForm(
                url = configuration.tokenEndpoint,
                formParameters = Parameters.build {
                    clientId(configuration.clientId)
                    clientSecret(checkNotNull(configuration.clientSecret) {
                        "Mangler verdi for 'clientSecret'"
                    })
                    builder()
                },
            )
        return when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.BadRequest -> openIDError("Ugyldig forespørsel, status: '${response.status}', body: '${response.body<Map<String, Any?>>()}'")
            else -> openIDError("Noe gikk galt, status: '${response.status}', body: '${response.bodyAsText()}'")
        }
    }

    suspend fun grant(scope: String): TokenSet = grant {
        grantType(GrantType.CLIENT_CREDENTIALS)
        scope(scope)
    }

    suspend fun grant(scope: String, onBehalfOf: String): TokenSet = grant {
        grantType(GrantType.JWT_BEARER)
        scope(scope)
        assertion(onBehalfOf)
        requestedTokenUse("on_behalf_of")
    }
}
