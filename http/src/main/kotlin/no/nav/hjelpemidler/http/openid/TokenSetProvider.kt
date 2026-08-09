package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.HttpRequestBuilder

/**
 * Hent [TokenSet] (evt. basert på [HttpRequestBuilder]/[no.nav.hjelpemidler.security.AuthenticatedPrincipal]).
 *
 * @see [HttpRequestBuilder.target]
 * @see [HttpRequestBuilder.asApplication]
 * @see [HttpRequestBuilder.onBehalfOf]
 * @see [no.nav.hjelpemidler.security.AuthenticatedPrincipal]
 */
fun interface TokenSetProvider {
    suspend operator fun invoke(request: HttpRequestBuilder): TokenSet
}
