package no.nav.hjelpemidler.http.openid

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.api.createClientPlugin

class OpenIDPluginConfiguration {
    var scope: String = ""
    var openIDClient: OpenIDClient? = null
    var onBehalfOfProvider: suspend () -> String? = { null }

    internal val openIDClientConfiguration: OpenIDClientConfiguration = OpenIDClientConfiguration()
    fun openIDClient(block: OpenIDClientConfiguration.() -> Unit) {
        openIDClientConfiguration.apply(block)
    }
}

val OpenIDPlugin = createClientPlugin("OpenIDPlugin", ::OpenIDPluginConfiguration) {
    val scope = pluginConfig.scope
    val openIDClient = when (val openIDClient = pluginConfig.openIDClient) {
        null -> createOpenIDClient(
            engine = client.engine,
            configuration = pluginConfig.openIDClientConfiguration,
        )

        else -> openIDClient
    }
    val onBehalfOfProvider = pluginConfig.onBehalfOfProvider
    onRequest { request, _ ->
        val tokenSet = when (val accessToken: String? = onBehalfOfProvider()) {
            null -> openIDClient.grant(scope = scope)
            else -> openIDClient.grant(scope = scope, onBehalfOf = accessToken)
        }
        request.bearerAuth(tokenSet)
    }
}

fun HttpClientConfig<*>.openID(
    scope: String,
    openIDClient: OpenIDClient? = null,
    block: OpenIDPluginConfiguration.() -> Unit = {},
) {
    install(OpenIDPlugin) {
        this.scope = scope
        this.openIDClient = openIDClient
        block()
    }
}

fun HttpClientConfig<*>.azureAD(scope: String, block: OpenIDClientConfiguration.() -> Unit = {}) {
    openID(scope = scope) {
        openIDClient {
            azureADEnvironmentConfiguration()
            block()
        }
    }
}

fun HttpClientConfig<*>.tokenX(scope: String, block: OpenIDClientConfiguration.() -> Unit = {}) {
    openID(scope = scope) {
        openIDClient {
            tokenXEnvironmentConfiguration()
            block()
        }
    }
}
