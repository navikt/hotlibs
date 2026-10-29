package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPayloadHolder

sealed class TexasCredential(credential: JWTCredential) : JWTPayloadHolder(credential.payload) {
    fun mustGet(name: String) = get(name) ?: error("'$name'-claim mangler")
}
