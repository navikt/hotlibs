package no.nav.hjelpemidler.server.ktor.security

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import no.nav.hjelpemidler.configuration.EntraIdEnvironmentVariable
import no.nav.hjelpemidler.configuration.TokenXEnvironmentVariable
import no.nav.hjelpemidler.text.toURL

class EntraIdJwkProvider(private val delegate: JwkProvider) : JwkProvider by delegate {
    constructor() : this(
        delegate = JwkProviderBuilder(EntraIdEnvironmentVariable.AZURE_OPENID_CONFIG_JWKS_URI.toURL())
            .cached(true)
            .rateLimited(true)
            .build()
    )
}

class TokenXJwkProvider(private val delegate: JwkProvider) : JwkProvider by delegate {
    constructor() : this(
        delegate = JwkProviderBuilder(TokenXEnvironmentVariable.TOKEN_X_JWKS_URI.toURL())
            .cached(true)
            .rateLimited(true)
            .build()
    )
}
