package no.nav.hjelpemidler.server.ktor.security

import io.kotest.matchers.shouldBe
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.test.TestResult
import no.nav.hjelpemidler.server.ktor.security.test.TestJwkProvider
import no.nav.hjelpemidler.server.ktor.security.test.lagAutentisertInnbygger
import org.junit.jupiter.api.Test

class TokenXAuthenticationTest {
    @Test
    fun `401 hvis token mangler`() = runTest {
        val response = client.get("/api/saker/1")

        response.status shouldBe HttpStatusCode.Unauthorized
    }

    @Test
    fun `200 hvis gyldig token`() = runTest {
        val innbygger = lagAutentisertInnbygger()

        val response = client.get("/api/saker/1") {
            bearerAuth(innbygger.userToken)
        }

        response.status shouldBe HttpStatusCode.OK
    }
}

private fun runTest(block: suspend ApplicationTestBuilder.() -> Unit): TestResult = testApplication {
    application {
        authentication {
            tokenX(jwkProvider = TestJwkProvider)
        }
    }

    routing {
        authenticate(AuthenticationProviderName.TOKEN_X) {
            get("/api/saker/{id}") {
                call.response.header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                call.respond("[]")
            }
        }
    }

    block()
}
