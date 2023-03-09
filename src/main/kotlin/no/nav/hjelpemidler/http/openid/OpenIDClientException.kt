package no.nav.hjelpemidler.http.openid

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters

internal class OpenIDClientException(message: String, cause: Throwable? = null) :
    RuntimeException(message, cause)

internal fun openIDError(message: String, cause: Throwable? = null): Nothing =
    throw OpenIDClientException(message, cause)

internal suspend fun openIDError(formParameters: Parameters, message: String, response: HttpResponse): Nothing {
    openIDError("$message, status: '${response.status}', body: '${response.bodyAsText()}', scope: '${formParameters.scope}'")
}
