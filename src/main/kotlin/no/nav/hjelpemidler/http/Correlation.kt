package no.nav.hjelpemidler.http

import io.ktor.client.request.header
import io.ktor.http.HttpMessageBuilder
import mu.withLoggingContext
import no.nav.hjelpemidler.configuration.NaisEnvironmentVariable
import org.slf4j.MDC
import java.util.UUID

const val CORRELATION_ID_KEY = "CorrelationId"

fun createCorrelationId(): String =
    UUID.randomUUID().toString()

fun currentCorrelationId(): String? =
    MDC.get(CORRELATION_ID_KEY)

fun HttpMessageBuilder.navConsumerId(value: String = NaisEnvironmentVariable.NAIS_APP_NAME) =
    header("Nav-Consumer-Id", value)

fun HttpMessageBuilder.navCallId(value: String) =
    header("Nav-CallId", value)

fun HttpMessageBuilder.navCorrelationId(value: String) =
    header("X-Correlation-ID", value)

fun HttpMessageBuilder.correlationId(value: String = currentCorrelationId() ?: createCorrelationId()): String {
    navCallId(value)
    navCorrelationId(value)
    navConsumerId()
    return value
}

inline fun <T> withCorrelationId(
    vararg pair: Pair<String, String?>,
    restorePrevious: Boolean = true,
    body: () -> T,
): T =
    withLoggingContext(
        pair = arrayOf(Pair<String, String?>(CORRELATION_ID_KEY, createCorrelationId())).plus(pair),
        restorePrevious = restorePrevious,
        body = body,
    )
