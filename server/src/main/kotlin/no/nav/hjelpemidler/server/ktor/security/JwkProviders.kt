package no.nav.hjelpemidler.server.ktor.security

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import no.nav.hjelpemidler.configuration.EntraIdEnvironmentVariable
import no.nav.hjelpemidler.configuration.IDPortenEnvironmentVariable
import no.nav.hjelpemidler.configuration.MaskinportenEnvironmentVariable
import no.nav.hjelpemidler.configuration.TokenXEnvironmentVariable
import java.net.URI

internal fun jwkProvider(jwksUri: String): JwkProvider = JwkProviderBuilder(URI(jwksUri).toURL())
    .cached(true)
    .rateLimited(true)
    .build()

class EntraIdJwkProvider(private val delegate: JwkProvider) : JwkProvider by delegate {
    constructor() : this(delegate = jwkProvider(EntraIdEnvironmentVariable.AZURE_OPENID_CONFIG_JWKS_URI))
}

class IDPortenJwkProvider(private val delegate: JwkProvider) : JwkProvider by delegate {
    constructor() : this(delegate = jwkProvider(IDPortenEnvironmentVariable.IDPORTEN_JWKS_URI))
}

class MaskinportenJwkProvider(private val delegate: JwkProvider) : JwkProvider by delegate {
    constructor() : this(delegate = jwkProvider(MaskinportenEnvironmentVariable.MASKINPORTEN_JWKS_URI))
}

class TokenXJwkProvider(private val delegate: JwkProvider) : JwkProvider by delegate {
    constructor() : this(delegate = jwkProvider(TokenXEnvironmentVariable.TOKEN_X_JWKS_URI))
}
