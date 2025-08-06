package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.HttpRequestBuilder

/**
 * Hent [TokenSet] (evt. basert på request).
 */
fun interface TokenSetProvider : suspend (HttpRequestBuilder) -> TokenSet
