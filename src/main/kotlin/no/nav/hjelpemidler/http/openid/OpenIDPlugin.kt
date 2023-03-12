package no.nav.hjelpemidler.http.openid

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.api.createClientPlugin
import kotlinx.coroutines.currentCoroutineContext

class OpenIDPluginConfiguration {
    var scope: String = ""
    internal var clientConfiguration: OpenIDClientConfiguration = OpenIDClientConfiguration()

    fun client(block: OpenIDClientConfiguration.() -> Unit) {
        clientConfiguration.apply(block)
    }
}

val OpenIDPlugin = createClientPlugin("OpenIDPlugin", ::OpenIDPluginConfiguration) {
    val scope = pluginConfig.scope
    val client = createOpenIDClient(
        engine = client.engine,
        configuration = pluginConfig.clientConfiguration,
    )
    onRequest { request, _ ->
        val openIDContext = currentCoroutineContext().openIDContext()
        val tokenSet = when (val accessToken = openIDContext.accessToken) {
            null -> client.grant(scope = scope)
            else -> client.grant(scope = scope, onBehalfOf = accessToken)
        }
        request.bearerAuth(tokenSet)
    }
}

fun HttpClientConfig<*>.openID(block: OpenIDPluginConfiguration.() -> Unit) {
    install(OpenIDPlugin) {
        block()
    }
}
