package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.auth.jwt.JWTCredential
import no.nav.hjelpemidler.http.openid.IdentityProvider

class MaskinportenCredential(credential: JWTCredential) : TexasCredential(IdentityProvider.MASKINPORTEN, credential)
