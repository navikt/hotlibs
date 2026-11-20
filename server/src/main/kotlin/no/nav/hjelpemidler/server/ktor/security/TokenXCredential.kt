package no.nav.hjelpemidler.server.ktor.security

import com.auth0.jwt.interfaces.Verification
import io.ktor.server.auth.jwt.JWTCredential
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.http.openid.IdentityProvider
import no.nav.hjelpemidler.security.IdPortenSecurityLevel

/**
 * @see <a href="https://docs.nais.io/auth/tokenx/reference/#claims">TokenX reference - Nais</a>
 * @see <a href="https://docs.digdir.no/docs/idporten/oidc/oidc_protocol_access_token#by-value--self-contained-access-token">access token from ID-porten | ID-porten</a>
 */
class TokenXCredential(credential: JWTCredential) : TexasCredential(IdentityProvider.TOKEN_X, credential) {
    val fnr: Fødselsnummer get() = mustGet(PID_CLAIM).let(::Fødselsnummer)
    val level: IdPortenSecurityLevel get() = mustGet(ACR_CLAIM).let(IdPortenSecurityLevel::of)

    companion object {
        /**
         * Sikkerhetsnivå.
         */
        const val ACR_CLAIM = "acr"

        /**
         * Fødselsnummer/D-nummer.
         */
        const val PID_CLAIM = "pid"
    }
}

fun Verification.withMinimumSecurityLevel(minimumLevel: IdPortenSecurityLevel): Verification =
    withClaim(TokenXCredential.ACR_CLAIM) { claim, _ ->
        val acrValue = claim.asString() ?: return@withClaim false
        val relativeValue = runCatching { IdPortenSecurityLevel.of(acrValue).relativeValue }.getOrDefault(Int.MIN_VALUE)
        relativeValue >= minimumLevel.relativeValue
    }
