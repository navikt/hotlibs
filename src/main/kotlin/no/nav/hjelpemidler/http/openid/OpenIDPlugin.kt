package no.nav.hjelpemidler.http.openid

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.bearerAuth
import io.ktor.http.ParametersBuilder
import kotlinx.coroutines.currentCoroutineContext

class OpenIDPluginConfiguration {
    var openIDClient: OpenIDClient = object : OpenIDClient {
        override suspend fun grant(builder: ParametersBuilder.() -> Unit): TokenSet =
            TokenSet("", 0, "")
    }
    var scope: String = ""
}

val OpenIDPlugin = createClientPlugin("OpenIDPlugin", ::OpenIDPluginConfiguration) {
    val openIDClient = pluginConfig.openIDClient
    val scope = pluginConfig.scope
    onRequest { request, _ ->
        val openIDContext = currentCoroutineContext().openIDContext()
        val tokenSet = when (val accessToken = openIDContext.accessToken) {
            null -> openIDClient.grant(scope = scope)
            else -> openIDClient.grant(scope = scope, onBehalfOf = accessToken)
        }
        request.bearerAuth(tokenSet.accessToken)
    }
}

fun HttpClientConfig<*>.azureAD(
    configuration: OpenIDConfiguration = azureADEnvironmentConfiguration(),
    engine: HttpClientEngine = CIO.create(),
    scope: String,
) =
    install(OpenIDPlugin) {
        this.openIDClient = azureADClient(configuration = configuration, engine = engine)
        this.scope = scope
    }
