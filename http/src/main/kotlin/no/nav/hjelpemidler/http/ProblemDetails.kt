package no.nav.hjelpemidler.http

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import no.nav.hjelpemidler.collections.mapOfNotNull
import no.nav.hjelpemidler.configuration.Environment
import java.net.URI
import java.net.URLEncoder

/**
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc9457">RFC 9457 - Problem Details for HTTP APIs</a>
 */
@JsonInclude(Include.NON_NULL)
data class ProblemDetails(
    val type: URI = DEFAULT_TYPE,
    val title: String? = null,
    @get:JsonSerialize(using = HttpStatusCodeSerializer::class)
    @get:JsonDeserialize(using = HttpStatusCodeDeserializer::class)
    val status: HttpStatusCode? = null,
    val detail: String? = null,
    val instance: URI? = null,
    @get:JsonAnyGetter
    @set:JsonAnySetter
    @get:JsonInclude(Include.NON_EMPTY, content = Include.NON_NULL)
    var extensions: Map<String, Any?> = emptyMap(),
) {
    constructor(
        status: HttpStatusCode,
        detail: String? = null,
        instance: URI? = null,
        extensions: Map<String, Any?> = emptyMap(),
    ) : this(
        type = HTTP_STATUS_CODE_TYPE,
        title = status.description,
        status = status,
        detail = detail,
        instance = instance,
        extensions = extensions,
    )

    constructor(
        throwable: Throwable,
        status: HttpStatusCode? = throwable.status,
        instance: URI? = null,
        extensions: Map<String, Any?> = emptyMap(),
    ) : this(
        type = throwable.type,
        title = throwable.title,
        status = status,
        detail = throwable.message,
        instance = instance,
        extensions = mapOfNotNull(
            "cause" to throwable.cause?.toString(),
            "stackTrace" to if (DEBUG) throwable.stackTraceToString() else null,
        ) + extensions
    )

    /**
     * Fjern [detail] hvis [HttpStatusCode.Unauthorized] eller [HttpStatusCode.Forbidden].
     */
    fun sanitize(): ProblemDetails {
        if (DEBUG) return this
        return when (status) {
            HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden -> copy(detail = null)
            else -> this
        }
    }

    fun toSpecification(): Map<String, Any?> = mapOfNotNull(
        "type" to type.toString(),
        "title" to title,
        "status" to status?.value,
        "detail" to detail,
        "instance" to instance?.toString(),
    ) + extensions

    companion object {
        val DEFAULT_TYPE: URI = URI.create("about:blank")
        val HTTP_STATUS_CODE_TYPE: URI = URI("io.ktor.http.HttpStatusCode")

        private val DEBUG: Boolean = !Environment.current.isProd
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

private class HttpStatusCodeSerializer : StdSerializer<HttpStatusCode>(HttpStatusCode::class.java) {
    override fun serialize(value: HttpStatusCode, generator: JsonGenerator, provider: SerializerProvider) =
        generator.writeNumber(value.value)
}

private class HttpStatusCodeDeserializer : StdDeserializer<HttpStatusCode>(HttpStatusCode::class.java) {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): HttpStatusCode =
        HttpStatusCode.fromValue(parser.intValue)
}

private val Throwable.type: URI
    get() = javaClass.name
        .replace("Kt$", "/")
        .replace('$', '/')
        .split('/')
        .joinToString("/") { URLEncoder.encode(it, Charsets.UTF_8) }
        .let(::URI)

private val Throwable.title: String?
    get() = javaClass.simpleName

private val Throwable.status: HttpStatusCode
    get() = if (this is HttpStatusCodeProvider) status else HttpStatusCode.InternalServerError
