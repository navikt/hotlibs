package no.nav.hjelpemidler.http.openid

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.api.createClientPlugin

class OpenIDPluginConfiguration {
    var scope: String = ""
    var tokenSetProvider: TokenSetProvider? = null

    internal val openIDClientConfiguration: OpenIDClientConfiguration = OpenIDClientConfiguration()
    fun openIDClient(block: OpenIDClientConfiguration.() -> Unit) {
        openIDClientConfiguration.apply(block)
    }
}

val OpenIDPlugin = createClientPlugin("OpenIDPlugin", ::OpenIDPluginConfiguration) {
    val tokenSetProvider = pluginConfig.tokenSetProvider ?: createOpenIDClient(
        engine = client.engine,
        configuration = pluginConfig.openIDClientConfiguration,
    ).withScope(pluginConfig.scope)
    onRequest { request, _ ->
        request.bearerAuth(tokenSetProvider())
    }
}

fun HttpClientConfig<*>.openID(
    tokenSetProvider: TokenSetProvider? = null,
    block: OpenIDPluginConfiguration.() -> Unit = {},
) {
    install(OpenIDPlugin) {
        this.tokenSetProvider = tokenSetProvider
        block()
    }
}

fun HttpClientConfig<*>.openID(
    scope: String,
    openIDClient: OpenIDClient? = null,
    block: OpenIDPluginConfiguration.() -> Unit = {},
) = openID(openIDClient?.withScope(scope), block)

fun HttpClientConfig<*>.azureAD(
    scope: String,
    engine: HttpClientEngine = CIO.create(),
    block: OpenIDClientConfiguration.() -> Unit = {},
) = openID(azureADClient(engine, block).withScope(scope))

fun HttpClientConfig<*>.tokenX(
    scope: String,
    engine: HttpClientEngine = CIO.create(),
    block: OpenIDClientConfiguration.() -> Unit = {},
) = openID(tokenXClient(engine, block).withScope(scope))
