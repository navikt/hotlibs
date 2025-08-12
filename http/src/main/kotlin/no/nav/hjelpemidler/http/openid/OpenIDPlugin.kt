package no.nav.hjelpemidler.http.openid

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.api.createClientPlugin

class OpenIDPluginConfiguration internal constructor() {
    var tokenSetProvider: TokenSetProvider? = null

    var identityProvider: IdentityProvider? = null
    var scope: String? = null
}

internal val OpenIDPlugin = createClientPlugin("OpenIDPlugin", ::OpenIDPluginConfiguration) {
    val tokenSetProvider = pluginConfig.tokenSetProvider ?: run {
        val identityProvider = pluginConfig.identityProvider
        val scope = pluginConfig.scope
        require(identityProvider != null && scope != null) {
            "Du må enten sette tokenSetProvider eller både identityProvider og scope"
        }
        TexasClient(client.engine).factory.delegate(identityProvider, defaultTarget = scope)
    }
    onRequest { request, _ ->
        request.bearerAuth(tokenSetProvider(request))
    }
}

fun HttpClientConfig<*>.openID(tokenSetProvider: TokenSetProvider) {
    install(OpenIDPlugin) {
        this.tokenSetProvider = tokenSetProvider
    }
}

fun HttpClientConfig<*>.openID(identityProvider: IdentityProvider, scope: String) {
    install(OpenIDPlugin) {
        this.identityProvider = identityProvider
        this.scope = scope
    }
}

fun HttpClientConfig<*>.entraID(scope: String) = openID(IdentityProvider.ENTRA_ID, scope)
fun HttpClientConfig<*>.maskinporten(scope: String) = openID(IdentityProvider.MASKINPORTEN, scope)
