package no.nav.hjelpemidler.http.openid

import com.auth0.jwt.interfaces.DecodedJWT
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import io.ktor.http.ParametersBuilder
import no.nav.hjelpemidler.configuration.MaskinportenEnvironmentVariable
import no.nav.hjelpemidler.time.toDate
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

interface OpenIDClient {
    suspend fun grant(builder: ParametersBuilder.() -> Unit): TokenSet

    suspend fun grant(scope: String): TokenSet = grant {
        grantType(GrantType.CLIENT_CREDENTIALS)
        scope(scope)
    }

    suspend fun grant(scope: String, onBehalfOf: String): TokenSet = grant {
        grantType(GrantType.JWT_BEARER)
        scope(scope)
        assertion(onBehalfOf)
        requestedTokenUse("on_behalf_of")
    }

    suspend fun grant(scope: String, onBehalfOf: DecodedJWT): TokenSet =
        grant(scope = scope, onBehalfOf = onBehalfOf.token)

    fun withScope(scope: String): TokenSetProvider = TokenSetProvider { grant(scope) }

    fun withMaskinportenAssertion(scope: String): TokenSetProvider {
        val rsaKey = RSAKey.parse(MaskinportenEnvironmentVariable.MASKINPORTEN_CLIENT_JWK)
        val header = JWSHeader.Builder(JWSAlgorithm.RS256)
            .keyID(rsaKey.keyID)
            .type(JOSEObjectType.JWT)
            .build()
        val signer = RSASSASigner(rsaKey.toPrivateKey())
        return TokenSetProvider {
            val signedJWT = SignedJWT(
                header,
                JWTClaimsSet.Builder()
                    .audience(MaskinportenEnvironmentVariable.MASKINPORTEN_ISSUER)
                    .issuer(MaskinportenEnvironmentVariable.MASKINPORTEN_CLIENT_ID)
                    .claim("scope", scope)
                    .issueTime(Date())
                    .expirationTime(LocalDateTime.now().plusSeconds(120).toDate())
                    .jwtID(UUID.randomUUID().toString())
                    .build(),
            )
            signedJWT.sign(signer)

            val assertion = signedJWT.serialize()
            grant(scope, assertion)
        }
    }
}
