package no.nav.hjelpemidler.http

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import no.nav.hjelpemidler.configuration.Environment
import tools.jackson.core.JsonGenerator
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.deser.std.StdScalarDeserializer
import tools.jackson.databind.ser.std.StdScalarSerializer
import java.net.URI

/**
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc9457">RFC 9457 - Problem Details for HTTP APIs</a>
 */
@JsonInclude(Include.NON_NULL)
data class ProblemDetails(
    val type: URI = DEFAULT_TYPE,
    val title: String? = null,
    @JsonSerialize(using = HttpStatusCodeSerializer::class)
    @JsonDeserialize(using = HttpStatusCodeDeserializer::class)
    val status: HttpStatusCode = HttpStatusCode.InternalServerError,
    val detail: String? = null,
    val instance: URI? = null,
    @JsonAnySetter
    @get:JsonAnyGetter
    @get:JsonInclude(Include.NON_EMPTY, content = Include.NON_NULL)
    val extensions: Map<String, Any?> = emptyMap(),
) {
    /**
     * Fjern [detail] hvis [HttpStatusCode.Unauthorized] eller [HttpStatusCode.Forbidden].
     */
    fun sanitize(): ProblemDetails =
        if (Environment.current.isProd && status in SENSITIVE_STATUSES) {
            copy(detail = null)
        } else {
            this
        }

    companion object {
        val DEFAULT_TYPE: URI = URI.create("https://teamdigihot.intern.nav.no/problems/unknown")

        private val SENSITIVE_STATUSES = setOf(
            HttpStatusCode.Unauthorized,
            HttpStatusCode.Forbidden,
        )
    }
}

suspend fun HttpResponse.problemDetails(): ProblemDetails? {
    val contentType = contentType() ?: return null
    return if (contentType.withoutParameters() == ContentType.Application.ProblemJson) {
        runCatching { body<ProblemDetails>() }
            .onFailure { log.warn(it) { "Kunne ikke lese ProblemDetails fra HttpResponse" } }
            .getOrNull()
    } else {
        null
    }
}

private val log = KotlinLogging.logger {}

private class HttpStatusCodeSerializer : StdScalarSerializer<HttpStatusCode>(HttpStatusCode::class.java) {
    override fun serialize(value: HttpStatusCode, generator: JsonGenerator, context: SerializationContext) {
        generator.writeNumber(value.value)
    }
}

private class HttpStatusCodeDeserializer : StdScalarDeserializer<HttpStatusCode>(HttpStatusCode::class.java) {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): HttpStatusCode =
        HttpStatusCode.fromValue(parser.intValue)
}

fun Throwable.asProblemDetailsExtensions(): Map<String, Any?> = mapOf(
    "cause" to cause?.toString(),
    "stackTrace" to stackTraceToString(),
)

class ProblemDetailsException(
    val details: ProblemDetails,
    cause: Throwable? = null,
) : RuntimeException(cause)

fun HttpClientConfig<*>.problemDetailsExceptionHandler() {
    HttpResponseValidator {
        handleResponseException { cause ->
            if (cause !is ResponseException) return@handleResponseException
            val details = cause.response.problemDetails() ?: return@handleResponseException
            throw ProblemDetailsException(details, cause)
        }
    }
}
