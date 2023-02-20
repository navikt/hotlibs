package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import no.nav.hjelpemidler.http.createHttpClient
import no.nav.hjelpemidler.http.test.respondJson
import no.nav.hjelpemidler.http.test.shouldBe
import kotlin.test.Test

class OpenIDPluginTest {
    @Test
    fun `skal hente og bruke access token fra klient`() {
        val engine = MockEngine {
            respondJson(
                """
                    {
                        "token_type": "Bearer",
                        "expires_in": 3600,
                        "access_token": ""
                    }
                """.trimIndent()
            )
        }
        val client = createHttpClient(engine = engine) {
            azureAD(engine = engine, scope = "test")
        }
        val response = runBlocking {
            client.get("/test") {
            }
        }
        response.status shouldBe HttpStatusCode.OK
    }
}
