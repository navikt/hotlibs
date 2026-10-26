package no.nav.hjelpemidler.http.openid

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.api.createClientPlugin

class OpenIdPluginConfiguration internal constructor() {
    var tokenSetProvider: TokenSetProvider? = null
    var identityProvider: IdentityProvider? = null
    var target: String? = null
}

internal val OpenIdPlugin = createClientPlugin("OpenIdPlugin", ::OpenIdPluginConfiguration) {
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
            IdentityProvider.ENTRA_ID -> texas.entraId(target)
            IdentityProvider.MASKINPORTEN -> texas.maskinporten(target)
            IdentityProvider.TOKEN_X -> texas.tokenX(target)
        }
    }
    onRequest { request, _ ->
        request.bearerAuth(tokenSetProvider(request))
    }
}

fun HttpClientConfig<*>.openId(tokenSetProvider: TokenSetProvider) {
    install(OpenIdPlugin) {
        this.tokenSetProvider = tokenSetProvider
    }
}

fun HttpClientConfig<*>.openId(identityProvider: IdentityProvider, target: String) {
    install(OpenIdPlugin) {
        this.identityProvider = identityProvider
        this.target = target
    }
}

fun HttpClientConfig<*>.entraId(target: String) = openId(IdentityProvider.ENTRA_ID, target)
fun HttpClientConfig<*>.tokenX(target: String) = openId(IdentityProvider.TOKEN_X, target)
fun HttpClientConfig<*>.maskinporten(target: String) = openId(IdentityProvider.MASKINPORTEN, target)
