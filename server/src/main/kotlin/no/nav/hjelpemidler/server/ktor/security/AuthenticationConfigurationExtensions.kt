package no.nav.hjelpemidler.server.ktor.security

import com.auth0.jwk.JwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import com.auth0.jwt.interfaces.Verification
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.jwt.jwt
import no.nav.hjelpemidler.configuration.ClusterEnvironment
import no.nav.hjelpemidler.configuration.EntraIdEnvironmentVariable
import no.nav.hjelpemidler.configuration.Environment
import no.nav.hjelpemidler.configuration.TokenXEnvironmentVariable

object AuthenticationConfiguration {
    const val ENTRA_ID = "ENTRA_ID"
    const val TOKEN_X = "TOKEN_X"
}

fun AuthenticationConfig.entraId(
    name: String = AuthenticationConfiguration.ENTRA_ID,
    jwkProvider: JwkProvider = EntraIdJwkProvider(),
    authenticatedPrincipalProvider: AuthenticatedPrincipalProvider<EntraIdCredential> = DefaultEntraIdAuthenticatedPrincipalProvider(),
    block: Verification.() -> Unit = {},
) {
    jwt(name) {
        if (Environment.current.isLocal) {
            verifier(LocalJWTVerifier)
        } else {
            verifier(jwkProvider, EntraIdEnvironmentVariable.AZURE_OPENID_CONFIG_ISSUER) {
                withAudience(EntraIdEnvironmentVariable.AZURE_APP_CLIENT_ID)
                block()
            }
        }
        validate { credential ->
            authenticatedPrincipalProvider(call = this, credential = EntraIdCredential(credential))
        }
    }
}

fun AuthenticationConfig.tokenX(
    name: String = AuthenticationConfiguration.TOKEN_X,
    jwkProvider: JwkProvider = TokenXJwkProvider(),
    authenticatedPrincipalProvider: AuthenticatedPrincipalProvider<TokenXCredential> = DefaultTokenXAuthenticatedPrincipalProvider(),
    block: Verification.() -> Unit = {},
) {
    jwt(name) {
        if (Environment.current.isLocal) {
            verifier(LocalJWTVerifier)
        } else {
            verifier(jwkProvider, TokenXEnvironmentVariable.TOKEN_X_ISSUER) {
                withAudience(TokenXEnvironmentVariable.TOKEN_X_CLIENT_ID)
                block()
            }
        }
        validate { credential ->
            authenticatedPrincipalProvider(call = this, credential = TokenXCredential(credential))
        }
    }
}

internal object LocalJWTVerifier : JWTVerifier {
    init {
        check(Environment.current !is ClusterEnvironment) { "LocalJWTVerifier kan bare brukes lokalt" }
    }

    override fun verify(token: String): DecodedJWT = JWT.decode(token)
    override fun verify(jwt: DecodedJWT): DecodedJWT = jwt
}
