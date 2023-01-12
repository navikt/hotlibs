package no.nav.hjelpemidler.http.openid

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.bearerAuth

class OpenIDPluginConfiguration {
    var openIDClient: OpenIDClient? = null
}

val OpenIDPlugin = createClientPlugin("OpenIDPlugin", ::OpenIDPluginConfiguration) {
    onRequest { request, _ ->
        request.bearerAuth("foobar")
        TODO("Ikke ferdig!")
    }
}
