package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.HttpRequestBuilder

/**
 * Hent [TokenSet] (evt. basert på [HttpRequestBuilder]/[OpenIDContext]).
 *
 * @see [HttpRequestBuilder.target]
 * @see [HttpRequestBuilder.onBehalfOf]
 * @see [HttpRequestBuilder.asApplication]
 * @see [OpenIDContext]
 */
fun interface TokenSetProvider {
    suspend operator fun invoke(request: HttpRequestBuilder): TokenSet
}
