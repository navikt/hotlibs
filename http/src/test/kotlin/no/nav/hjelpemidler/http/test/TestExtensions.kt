package no.nav.hjelpemidler.http.test

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.json.JsonMapper
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import no.nav.hjelpemidler.serialization.defaultJsonMapper
import org.intellij.lang.annotations.Language

val jsonMapper: JsonMapper = defaultJsonMapper()

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

fun createJWT(block: JWTCreator.Builder.() -> Unit = {}): String =
    JWT.create()
        .apply(block)
        .sign(Algorithm.HMAC256("secret"))
