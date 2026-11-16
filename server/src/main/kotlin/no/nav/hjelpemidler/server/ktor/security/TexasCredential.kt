package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPayloadHolder
import no.nav.hjelpemidler.http.openid.IdentityProvider

sealed class TexasCredential(
    val identityProvider: IdentityProvider,
    credential: JWTCredential,
) : JWTPayloadHolder(credential.payload) {
    fun mustGet(name: String): String = get(name) ?: error("'$name'-claim mangler")
}
