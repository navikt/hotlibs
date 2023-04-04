package no.nav.hjelpemidler.http.openid

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.api.createClientPlugin

class OpenIDPluginConfiguration {
    internal var scope: String = ""
    internal var client: OpenIDClient? = null
    internal val clientConfiguration: OpenIDClientConfiguration = OpenIDClientConfiguration()

    fun client(block: OpenIDClientConfiguration.() -> Unit) {
        clientConfiguration.apply(block)
    }

    var accessTokenProvider: suspend () -> String? = { null }
}

val OpenIDPlugin = createClientPlugin("OpenIDPlugin", ::OpenIDPluginConfiguration) {
    val scope = pluginConfig.scope
    val engine = client.engine
    val client = when (val client = pluginConfig.client) {
        null -> createOpenIDClient(
            engine = engine,
            configuration = pluginConfig.clientConfiguration,
        )

        else -> client
    }
    val accessTokenProvider = pluginConfig.accessTokenProvider
    onRequest { request, _ ->
        val tokenSet = when (val accessToken: String? = accessTokenProvider()) {
            null -> client.grant(scope = scope)
            else -> client.grant(scope = scope, onBehalfOf = accessToken)
        }
        request.bearerAuth(tokenSet)
    }
}

fun HttpClientConfig<*>.openID(
    scope: String,
    client: OpenIDClient? = null,
    block: OpenIDPluginConfiguration.() -> Unit,
) {
    install(OpenIDPlugin) {
        this.scope = scope
        this.client = client
        block()
    }
}

fun HttpClientConfig<*>.azureAD(scope: String, block: OpenIDClientConfiguration.() -> Unit) {
    openID(scope = scope) {
        client {
            azureADEnvironmentConfiguration()
            block()
        }
    }
}

fun HttpClientConfig<*>.tokenX(scope: String, block: OpenIDClientConfiguration.() -> Unit) {
    openID(scope = scope) {
        client {
            tokenXEnvironmentConfiguration()
            block()
        }
    }
}
