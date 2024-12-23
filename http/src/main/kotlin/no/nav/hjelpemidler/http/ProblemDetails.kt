package no.nav.hjelpemidler.http

import java.net.URI

data class ProblemDetails(
    val type: URI = URI.create("about:blank"),
)
