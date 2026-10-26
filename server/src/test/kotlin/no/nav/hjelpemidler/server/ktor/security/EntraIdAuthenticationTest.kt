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
import no.nav.hjelpemidler.server.ktor.security.test.applikasjonToken
import no.nav.hjelpemidler.server.ktor.security.test.lagAutentisertAnsatt
import org.junit.jupiter.api.Test

class EntraIdAuthenticationTest {
    @Test
    fun `401 hvis token mangler`() = runTest {
        val response = client.get("/api/saker/1")

        response.status shouldBe HttpStatusCode.Unauthorized
    }

    @Test
    fun `200 hvis gyldig token for ansatt`() = runTest {
        val ansatt = lagAutentisertAnsatt()

        val response = client.get("/api/saker/1") {
            bearerAuth(ansatt.userToken)
        }

        response.status shouldBe HttpStatusCode.OK
    }

    @Test
    fun `200 hvis gyldig token for applikasjon`() = runTest {
        val response = client.get("/api/saker/1") {
            bearerAuth(applikasjonToken)
        }

        response.status shouldBe HttpStatusCode.OK
    }
}

private fun runTest(block: suspend ApplicationTestBuilder.() -> Unit): TestResult = testApplication {
    application {
        authentication {
            entraId(jwkProvider = TestJwkProvider)
        }
    }

    routing {
        authenticate(AuthenticationConfiguration.ENTRA_ID) {
            get("/api/saker/{id}") {
                call.response.header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                call.respond("[]")
            }
        }
    }

    block()
}
