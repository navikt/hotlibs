package no.nav.hjelpemidler.server.ktor

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentLength
import io.ktor.http.contentType
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.copyTo

/** Headers from the incoming request to send on to the backend. */
fun ApplicationCall.upstreamHeaders(): Headers = Headers.build {
    request.headers.forEach { name, values ->
        if (name.lowercase() in upstreamRequestHeaders) appendAll(name, values)
    }
    append(HttpHeaders.XForwardedFor, request.local.remoteAddress)
}

/** Sends the backend response (status, allowed headers, body) back to the caller. */
suspend fun ApplicationCall.respondUpstream(upstream: HttpResponse) {
    val forwarded = Headers.build {
        upstream.headers.forEach { name, values ->
            if (name.lowercase() in downstreamResponseHeaders) appendAll(name, values)
        }
    }

    if (upstream.status == HttpStatusCode.NoContent || upstream.status == HttpStatusCode.NotModified) {
        respond(object : OutgoingContent.NoContent() {
            override val status = upstream.status
            override val headers = forwarded
        })
        return
    }

    respond(object : OutgoingContent.WriteChannelContent() {
        override val status = upstream.status
        override val contentType = upstream.contentType()
        override val contentLength = upstream.contentLength()
        override val headers = forwarded

        override suspend fun writeTo(channel: ByteWriteChannel) {
            upstream.bodyAsChannel().copyTo(channel)
        }
    })
}

private val upstreamRequestHeaders = setOf(
    HttpHeaders.Accept,
    HttpHeaders.AcceptLanguage,
    HttpHeaders.IfModifiedSince,
    HttpHeaders.IfNoneMatch,
    HttpHeaders.IfRange,
    HttpHeaders.Range,
).map { it.lowercase() }.toSet()

private val downstreamResponseHeaders = setOf(
    HttpHeaders.AcceptRanges,
    HttpHeaders.CacheControl,
    HttpHeaders.ContentDisposition,
    HttpHeaders.ContentEncoding,
    HttpHeaders.ContentRange,
    HttpHeaders.ETag,
    HttpHeaders.LastModified,
    HttpHeaders.RetryAfter,
).map { it.lowercase() }.toSet()
