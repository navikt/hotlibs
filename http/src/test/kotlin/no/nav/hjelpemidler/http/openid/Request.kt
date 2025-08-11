package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.HttpRequestBuilder

fun request(block: HttpRequestBuilder.() -> Unit = {}): HttpRequestBuilder = HttpRequestBuilder().apply(block)
