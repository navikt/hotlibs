package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.HttpRequestBuilder

/**
 * Hent [TokenSet] (evt. basert på [HttpRequestBuilder]/[AuthenticatedPrincipal]).
 *
 * @see [HttpRequestBuilder.target]
 * @see [HttpRequestBuilder.asApplication]
 * @see [HttpRequestBuilder.onBehalfOf]
 * @see [AuthenticatedPrincipal]
 */
fun interface TokenSetProvider {
    suspend operator fun invoke(request: HttpRequestBuilder): TokenSet
}
