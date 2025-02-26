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
                          "type": "about:blank",
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
    fun `Lager forventet JSON`() {
        val throwable = TestException(RuntimeException("Og dette er grunnen!"))
        val details = ProblemDetails(throwable)

        val detailsJson = valueToJson(details)

        detailsJson shouldEqualSpecifiedJson """
            {
              "type" : "no.nav.hjelpemidler.http.TestException",
              "title" : "TestException",
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
