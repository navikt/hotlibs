package no.nav.hjelpemidler.http.openid

internal class OpenIDClientException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

internal fun openIDError(message: String, cause: Throwable? = null): Nothing =
    throw OpenIDClientException(message, cause)
