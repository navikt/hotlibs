package no.nav.hjelpemidler.http.openid

import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondError
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import no.nav.hjelpemidler.http.createHttpClient
import no.nav.hjelpemidler.http.test.respondJson
import kotlin.test.Test
import kotlin.time.Duration.Companion.hours

class OpenIDPluginTest {
    @Test
    fun `skal hente og bruke access token fra klient`() {
        val engine = MockEngine {
            when {
                it.url.toString().endsWith("/token") ->
                    respondJson(TokenSet.bearer(1.hours, "token"))

                it.url.toString().endsWith("/test") -> {
                    it.headers["Authorization"] shouldBe "Bearer token"
                    respondOk()
                }

                else ->
                    respondError(HttpStatusCode.NotFound)
            }
        }
        val client = createHttpClient(engine = engine) {
            openID(scope = "test") {
                client {
                    tokenEndpoint = "https://issuer/token"
                    clientId = "clientId"
                    clientSecret = "clientSecret"

                    cache()
                }
            }
        }
        val response = runBlocking {
            client.get("/test") {
            }
        }
        response shouldHaveStatus HttpStatusCode.OK
    }
}
