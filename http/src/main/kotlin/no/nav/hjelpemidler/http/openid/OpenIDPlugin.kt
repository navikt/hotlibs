package no.nav.hjelpemidler.http.openid

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.api.createClientPlugin

class OpenIDPluginConfiguration internal constructor() {
    var tokenSetProvider: TokenSetProvider? = null
    var identityProvider: IdentityProvider? = null
    var target: String? = null
}

internal val OpenIDPlugin = createClientPlugin("OpenIDPlugin", ::OpenIDPluginConfiguration) {
    val tokenSetProvider = pluginConfig.tokenSetProvider ?: run {
        val identityProvider = pluginConfig.identityProvider
        val target = pluginConfig.target
        require(identityProvider != null && target != null) {
            "Du må enten sette tokenSetProvider eller både identityProvider og target"
        }
        require(identityProvider != IdentityProvider.ID_PORTEN) {
            "identityProvider kan ikke være $identityProvider"
        }

        val texas = texasProvider(client.engine)
        if (texas is AutoCloseable && texasProvider is DefaultTexasProvider) {
            onClose(texas::close)
        }
        when (identityProvider) {
            IdentityProvider.ENTRA_ID -> texas.entraID(target)
            IdentityProvider.MASKINPORTEN -> texas.maskinporten(target)
            IdentityProvider.TOKEN_X -> texas.tokenX(target)
        }
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

fun HttpClientConfig<*>.openID(identityProvider: IdentityProvider, target: String) {
    install(OpenIDPlugin) {
        this.identityProvider = identityProvider
        this.target = target
    }
}

fun HttpClientConfig<*>.entraID(target: String) = openID(IdentityProvider.ENTRA_ID, target)
fun HttpClientConfig<*>.tokenX(target: String) = openID(IdentityProvider.TOKEN_X, target)
fun HttpClientConfig<*>.maskinporten(target: String) = openID(IdentityProvider.MASKINPORTEN, target)
