package no.nav.hjelpemidler.server.ktor.security

import com.auth0.jwk.JwkProvider
import com.auth0.jwt.interfaces.Verification
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.jwt.jwt
import no.nav.hjelpemidler.collections.enumSetOf
import no.nav.hjelpemidler.configuration.EntraIdEnvironmentVariable
import no.nav.hjelpemidler.configuration.TokenXEnvironmentVariable
import no.nav.hjelpemidler.http.openid.IdentityProvider
import no.nav.hjelpemidler.http.openid.Texas

object AuthenticationProviderName {
    const val ENTRA_ID = "ENTRA_ID"
    const val TOKEN_X = "TOKEN_X"
    const val TEXAS = "TEXAS"
}

fun AuthenticationConfig.entraId(
    name: String = AuthenticationProviderName.ENTRA_ID,
    jwkProvider: JwkProvider = EntraIdJwkProvider(),
    authenticatedPrincipalProvider: EntraIdAuthenticatedPrincipalProvider = DefaultEntraIdAuthenticatedPrincipalProvider(),
    block: Verification.() -> Unit = {},
) {
    jwt(name) {
        verifier(jwkProvider, EntraIdEnvironmentVariable.AZURE_OPENID_CONFIG_ISSUER) {
            withAudience(EntraIdEnvironmentVariable.AZURE_APP_CLIENT_ID)
            block()
        }
        validate { credential ->
            authenticatedPrincipalProvider(call = this, credential = EntraIdCredential(credential))
        }
    }
}

fun AuthenticationConfig.tokenX(
    name: String = AuthenticationProviderName.TOKEN_X,
    jwkProvider: JwkProvider = TokenXJwkProvider(),
    authenticatedPrincipalProvider: TokenXAuthenticatedPrincipalProvider = DefaultTokenXAuthenticatedPrincipalProvider(),
    block: Verification.() -> Unit = {},
) {
    jwt(name) {
        verifier(jwkProvider, TokenXEnvironmentVariable.TOKEN_X_ISSUER) {
            withAudience(TokenXEnvironmentVariable.TOKEN_X_CLIENT_ID)
            block()
        }
        validate { credential ->
            authenticatedPrincipalProvider(call = this, credential = TokenXCredential(credential))
        }
    }
}

fun AuthenticationConfig.texas(
    texas: Texas,
    name: String = AuthenticationProviderName.TEXAS,
    identityProviders: Set<IdentityProvider> = enumSetOf(IdentityProvider.ENTRA_ID, IdentityProvider.TOKEN_X),
    authenticatedPrincipalProvider: AuthenticatedPrincipalProvider<TexasCredential> = DefaultTexasAuthenticatedPrincipalProvider(),
    block: Verification.(IdentityProvider) -> Unit = {},
) {
    jwt(name) {
        verifier(TexasVerifier(texas, identityProviders, block))
        validate { credential ->
            val identityProvider = credential.issuer?.let(IdentityProvider::forIssuer) ?: return@validate null
            authenticatedPrincipalProvider(
                call = this,
                credential = when (identityProvider) {
                    IdentityProvider.ENTRA_ID -> EntraIdCredential(credential)
                    IdentityProvider.ID_PORTEN -> IDPortenCredential(credential)
                    IdentityProvider.MASKINPORTEN -> MaskinportenCredential(credential)
                    IdentityProvider.TOKEN_X -> TokenXCredential(credential)
                }
            )
        }
    }
}
