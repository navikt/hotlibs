package no.nav.hjelpemidler.test.ktor

import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.http.createHttpClient
import kotlin.test.Test

class StubEngineTest {
    private val body = mapOf("id" to "1", "name" to "Test")

    @Test
    fun `StubEngine skal gi riktig svar`() = runTest {
        val engine = StubEngine {
            baseUrl("http://service:8080/api") {
                routing {
                    get("$it/user") {
                        call.respond(body)
                    }
                }
            }
        }
        val client = createHttpClient(engine)

        val responseBody = client
            .get("http://service:8080/api/user")
            .body<Map<String, Any?>>()

        responseBody shouldBe body
    }
}
