package no.nav.hjelpemidler.server.ktor.security

import io.ktor.server.auth.jwt.JWTCredential
import no.nav.hjelpemidler.http.openid.IdentityProvider

class IDPortenCredential(credential: JWTCredential) : TexasCredential(IdentityProvider.ID_PORTEN, credential)
