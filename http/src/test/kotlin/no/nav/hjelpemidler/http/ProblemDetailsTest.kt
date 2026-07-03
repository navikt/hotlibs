package no.nav.hjelpemidler.http

import io.kotest.assertions.json.shouldContainJsonKey
import io.kotest.assertions.json.shouldEqualSpecifiedJson
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import kotlin.test.Test

class ProblemDetailsTest {
    private val client = createHttpClient(MockEngine) {
        engine {
            addHandler {
                respond(
                    //language=JSON
                    """
                        {
                          "type": "https://teamdigihot.intern.nav.no/problems/unknown",
                          "status": 401,
                          "cause": "Ingen tilgang!"
                        }
                    """.trimIndent(),
                    HttpStatusCode.OK,
                    headersOf(HttpHeaders.ContentType, "${ContentType.Application.ProblemJson}")
                )
            }
        }
    }

    @Test
    fun `Kan lese ProblemDetails fra HttpResponse`() = runTest {
        val response = client.get("http://localhost/test")

        val details = response.problemDetails().shouldNotBeNull()

        details.type shouldBe ProblemDetails.DEFAULT_TYPE
        details.status shouldBe HttpStatusCode.Unauthorized
        details.extensions.shouldContain("cause" to "Ingen tilgang!")
    }

    @Test
    fun `Lager forventet JSON for status`() {
        val status = HttpStatusCode.Forbidden
        val details = ProblemDetails(
            title = status.description,
            status = status,
            detail = "Ingen tilgang!",
            extensions = mutableMapOf("test" to null)
        )

        val detailsJson = valueToJson(details)

        detailsJson shouldEqualSpecifiedJson """
            {
              "type" : "https://teamdigihot.intern.nav.no/problems/unknown",
              "title" : "Forbidden",
              "status" : 403,
              "detail": "Ingen tilgang!"
            }
        """.trimIndent()
    }

    @Test
    fun `Lager forventet JSON for throwable`() {
        val throwable = TestException(RuntimeException("Og dette er grunnen!"))
        val details = ProblemDetails(
            title = throwable.status.description,
            status = throwable.status,
            detail = throwable.message,
            extensions = throwable.asProblemDetailsExtensions(),
        )

        val detailsJson = valueToJson(details)

        detailsJson shouldEqualSpecifiedJson """
            {
              "type" : "https://teamdigihot.intern.nav.no/problems/unknown",
              "title" : "Service Unavailable",
              "status" : 503,
              "detail" : "Noe gikk galt!",
              "cause" : "java.lang.RuntimeException: Og dette er grunnen!"
            }
        """.trimIndent()
        detailsJson.shouldContainJsonKey("stackTrace")
    }
}

private class TestException(override val cause: Throwable?) : RuntimeException("Noe gikk galt!", cause),
    HttpStatusCodeProvider {
    override val status: HttpStatusCode get() = HttpStatusCode.ServiceUnavailable
}
