package no.nav.hjelpemidler.server.ktor.security.test

import com.auth0.jwk.Jwk
import com.auth0.jwk.JwkProvider
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import no.nav.hjelpemidler.configuration.EntraIdEnvironmentVariable
import no.nav.hjelpemidler.configuration.NaisEnvironmentVariable
import no.nav.hjelpemidler.configuration.TokenXEnvironmentVariable
import no.nav.hjelpemidler.security.AuthenticatedApplication
import no.nav.hjelpemidler.security.AuthenticatedPrincipal
import no.nav.hjelpemidler.server.ktor.security.AutentisertAnsatt
import no.nav.hjelpemidler.server.ktor.security.AutentisertInnbygger
import no.nav.hjelpemidler.server.ktor.security.EntraIdCredential
import no.nav.hjelpemidler.server.ktor.security.TokenXCredential
import no.nav.hjelpemidler.time.minutter
import java.security.interfaces.RSAPrivateKey
import java.time.Instant
import java.util.Date
import java.util.UUID
import com.auth0.jwt.JWT as Jwt

private val testRSAKey: RSAKey = RSAKeyGenerator(2048)
    .keyID(UUID.randomUUID().toString())
    .keyUse(KeyUse.SIGNATURE)
    .issueTime(Date())
    .generate()

private val testRSAPrivateKey: RSAPrivateKey = testRSAKey.toRSAPrivateKey()

private val testAlgorithm: Algorithm = Algorithm.RSA256(testRSAKey.toRSAPublicKey(), testRSAPrivateKey)

object TestJwkProvider : JwkProvider {
    private val jwk: Jwk = Jwk.fromValues(testRSAKey.toJSONObject())
    override fun get(keyId: String?): Jwk = jwk
}

/**
 * Konverter [AuthenticatedPrincipal] til JWT-token.
 */
fun AuthenticatedPrincipal.asToken(block: JWTCreator.Builder.() -> Unit = {}): String {
    val now = Instant.now()

    val builder = Jwt.create()
        .withKeyId(testRSAKey.keyID)
        .withSubject(UUID.randomUUID().toString())
        .withIssuedAt(now)
        .withExpiresAt(now + 10.minutter)

    when (this) {
        is AutentisertInnbygger -> builder
            .withIssuer(TokenXEnvironmentVariable.TOKEN_X_ISSUER)
            .withAudience(TokenXEnvironmentVariable.TOKEN_X_CLIENT_ID)
            .withClaim(TokenXCredential.ACR_CLAIM, level.toString())
            .withClaim(TokenXCredential.PID_CLAIM, fnr.toString())

        else -> {
            builder
                .withIssuer(EntraIdEnvironmentVariable.AZURE_OPENID_CONFIG_ISSUER)
                .withAudience(EntraIdEnvironmentVariable.AZURE_APP_CLIENT_ID)
                .withClaim(EntraIdCredential.AZP_NAME_CLAIM, NaisEnvironmentVariable.NAIS_APP_NAME)

            when (this) {
                is AuthenticatedApplication -> builder
                    .withClaim(EntraIdCredential.TOKEN_TYPE_CLAIM, EntraIdCredential.TOKEN_TYPE_APP)

                is AutentisertAnsatt -> builder
                    .withClaim(EntraIdCredential.Ansatt.NAV_IDENT_CLAIM, id.toString())
                    .withClaim(EntraIdCredential.Ansatt.NAME_CLAIM, navn)
                    .withClaim(EntraIdCredential.Ansatt.PREFERRED_USERNAME_CLAIM, epost)
                    .withClaim(EntraIdCredential.Ansatt.GROUPS_CLAIM, grupper.map { it.toString() })

                else -> TODO()
            }
        }
    }

    return builder.apply(block).sign(testAlgorithm)
}
