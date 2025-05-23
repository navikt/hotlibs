package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.HttpRequestBuilder

fun interface TokenSetProvider {
    suspend operator fun invoke(request: HttpRequestBuilder): TokenSet
}
