package no.nav.hjelpemidler.http.test

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import org.intellij.lang.annotations.Language
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

infix fun <T> T.shouldBe(expected: T) = assertEquals(expected, this)
infix fun <T> T.shouldNotBe(illegal: T) = assertNotEquals(illegal, this)

val jsonMapper: JsonMapper = jacksonMapperBuilder()
    .addModule(JavaTimeModule())
    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .build()

fun MockRequestHandleScope.respondJson(
    @Language("JSON") content: String,
    status: HttpStatusCode = HttpStatusCode.OK,
) =
    respond(
        content = content,
        status = status,
        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    )

fun MockRequestHandleScope.respondJson(
    value: Any?,
    status: HttpStatusCode = HttpStatusCode.OK,
) =
    respondJson(
        content = jsonMapper.writeValueAsString(value),
        status = status,
    )
