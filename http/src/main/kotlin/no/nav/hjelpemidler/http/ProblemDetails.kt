package no.nav.hjelpemidler.http

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonDeserializeAs
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import no.nav.hjelpemidler.collections.filterNotNull
import no.nav.hjelpemidler.collections.mapOfNotNull
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
@JsonDeserializeAs(DefaultProblemDetails::class)
interface ProblemDetails {
    val type: URI
    val title: String?

    @get:JsonSerialize(using = HttpStatusCodeSerializer::class)
    @get:JsonDeserialize(using = HttpStatusCodeDeserializer::class)
    val status: HttpStatusCode
    val detail: String?
    val instance: URI?

    companion object {
        val DEFAULT_TYPE: URI = URI.create("https://teamdigihot.intern.nav.no/problems/unknown")
    }
}

data class DefaultProblemDetails(
    override val type: URI = ProblemDetails.DEFAULT_TYPE,
    override val title: String? = null,
    override val status: HttpStatusCode = HttpStatusCode.InternalServerError,
    override val detail: String? = null,
    override val instance: URI? = null,
    @JsonAnySetter
    @get:JsonAnyGetter
    @get:JsonInclude(Include.NON_EMPTY, content = Include.NON_NULL)
    val extensions: Map<String, Any?> = emptyMap(),
) : ProblemDetails {
    /**
     * Fjern [detail] hvis [HttpStatusCode.Unauthorized] eller [HttpStatusCode.Forbidden].
     */
    fun sanitize(): DefaultProblemDetails =
        if (Environment.current.isProd && status in SENSITIVE_STATUSES) {
            copy(detail = null)
        } else {
            this
        }

    fun asMap(): Map<String, Any> = mapOfNotNull(
        "type" to type.toString(),
        "title" to title,
        "status" to status.value,
        "detail" to detail,
        "instance" to instance?.toString(),
    ) + extensions.filterNotNull()

    companion object {
        private val SENSITIVE_STATUSES = setOf(
            HttpStatusCode.Unauthorized,
            HttpStatusCode.Forbidden,
        )
    }
}

suspend inline fun <reified T : ProblemDetails> HttpResponse.problemDetails(): T? {
    val contentType = contentType() ?: return null
    return if (contentType.withoutParameters() == ContentType.Application.ProblemJson) {
        body<T>()
    } else {
        null
    }
}

@JvmName("defaultProblemDetails")
suspend fun HttpResponse.problemDetails() = problemDetails<DefaultProblemDetails>()

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

private val ProblemDetails.message: String?
    get() = when {
        title != null && detail != null -> "$title: $detail"
        title != null -> title
        detail != null -> detail
        else -> null
    }

class ProblemDetailsException(
    val details: ProblemDetails,
    cause: Throwable? = null,
) : RuntimeException(details.message, cause)

inline fun <reified T : ProblemDetails> HttpClientConfig<*>.problemDetailsExceptionHandler() {
    HttpResponseValidator {
        handleResponseException { cause ->
            if (cause !is ResponseException) return@handleResponseException
            val details = cause.response.problemDetails<T>() ?: return@handleResponseException
            throw ProblemDetailsException(details, cause)
        }
    }
}

@JvmName("defaultProblemDetailsExceptionHandler")
fun HttpClientConfig<*>.problemDetailsExceptionHandler() = problemDetailsExceptionHandler<DefaultProblemDetails>()
