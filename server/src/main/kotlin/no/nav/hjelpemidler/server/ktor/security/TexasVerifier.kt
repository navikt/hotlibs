package no.nav.hjelpemidler.server.ktor.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import com.auth0.jwt.interfaces.Verification
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.auth.HttpAuthHeader
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import no.nav.hjelpemidler.http.openid.IdentityProvider
import no.nav.hjelpemidler.http.openid.Texas

private val log = KotlinLogging.logger {}

internal class TexasVerifier(
    private val texas: Texas,
    private val identityProviders: Set<IdentityProvider>,
    private val configure: Verification.(IdentityProvider) -> Unit = {},
) : suspend (HttpAuthHeader) -> JWTVerifier? {
    override suspend fun invoke(token: HttpAuthHeader): JWTVerifier? {
        val bearerToken = token.bearerToken()
            ?: return reject { "No bearer token (scheme: '${token.authScheme}')" }
        val decoded = runCatching { JWT.decode(bearerToken) }.getOrNull()
            ?: return reject { "Bearer token is not a valid JWT" }
        val algorithm: String = decoded.algorithm
            ?: return reject { "Token has no algorithm" }
        val issuer = decoded.issuer
            ?: return reject { "Token has no issuer" }
        val identityProvider = IdentityProvider.forIssuer(issuer)
            ?: return reject { "Unknown issuer: '$issuer'" }
        if (identityProvider !in identityProviders) {
            return reject { "Issuer ${identityProvider.name} not accepted, accepted: ${identityProviders.joinToString { it.name }}" }
        }

        val introspection = try {
            texas.introspection(identityProvider, bearerToken)
        } catch (e: CancellationException) {
            currentCoroutineContext().ensureActive()
            log.warn(e) { "Texas introspection was cancelled (timeout?) for ${identityProvider.name}" }
            return null
        } catch (e: Exception) {
            log.error(e) { "Texas introspection failed for ${identityProvider.name}" }
            return null
        }
        if (!introspection.active) {
            return reject { "Token from ${identityProvider.name} not active: ${introspection.error}" }
        }

        val delegate = JWT.require(IntrospectedTokenAlgorithm(algorithm))
            .withIssuer(issuer)
            .apply { configure(identityProvider) }
            .build()

        return IntrospectedTokenVerifier(decoded, delegate)
    }

    private inline fun reject(crossinline message: () -> String): JWTVerifier? {
        log.debug { message() }
        return null
    }
}

/**
 * NB! Only used after Texas has confirmed that this exact token is active through introspection.
 *
 * The [name] must match the token's `alg`, otherwise `java-jwt` throws [com.auth0.jwt.exceptions.AlgorithmMismatchException].
 */
private class IntrospectedTokenAlgorithm(name: String) : Algorithm(name, "Signature verified by Texas") {
    override fun verify(jwt: DecodedJWT) = Unit

    override fun sign(headerBytes: ByteArray?, payloadBytes: ByteArray?): ByteArray =
        throw UnsupportedOperationException("Verification only")

    override fun sign(contentBytes: ByteArray?): ByteArray =
        throw UnsupportedOperationException("Verification only")
}

private class IntrospectedTokenVerifier(
    private val decoded: DecodedJWT,
    private val delegate: JWTVerifier,
) : JWTVerifier {
    override fun verify(token: String): DecodedJWT {
        if (token != decoded.token) throw JWTVerificationException("Token does not match introspected token")
        return delegate.verify(decoded)
    }

    override fun verify(jwt: DecodedJWT): DecodedJWT = verify(jwt.token)
}
