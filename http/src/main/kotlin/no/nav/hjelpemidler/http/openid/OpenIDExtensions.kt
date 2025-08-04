package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.Parameters

fun HttpMessageBuilder.bearerAuth(tokenSet: TokenSet): Unit =
    bearerAuth(tokenSet.accessToken)

internal val Parameters.grantType
    get() = get("grant_type")

internal val Parameters.scope
    get() = get("scope")
