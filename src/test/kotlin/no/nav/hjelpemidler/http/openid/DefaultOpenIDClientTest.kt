package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import no.nav.hjelpemidler.http.test.respondJson
import no.nav.hjelpemidler.http.test.shouldBe
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class DefaultOpenIDClientTest {
    @Test
    fun `client credentials grant`() {
        val client = createTestClient {
            respondJson(
                """
                {
                  "token_type": "Bearer",
                  "expires_in": 3599,
                  "access_token": "hemmelig"
                }
                """.trimIndent()
            )
        }
        val tokenSet = runBlocking(Dispatchers.IO) {
            client.grant(scope = "test")
        }

        tokenSet.tokenType shouldBe "Bearer"
        tokenSet.expiresIn shouldBe 3599
        tokenSet.accessToken shouldBe "hemmelig"
    }

    @Test
    fun `on behalf of grant`() {
        val client = createTestClient {
            respondJson(
                """
                {
                  "token_type": "Bearer",
                  "scope": "https://graph.microsoft.com/user.read",
                  "expires_in": 3269,
                  "ext_expires_in": 0,
                  "access_token": "hemmelig1",
                  "refresh_token": "hemmelig2"
                }
                """.trimIndent()
            )
        }
        val tokenSet = runBlocking(Dispatchers.IO) {
            client.grant(scope = "test", onBehalfOf = "test")
        }

        tokenSet.tokenType shouldBe "Bearer"
        tokenSet.expiresIn shouldBe 3269
        tokenSet.accessToken shouldBe "hemmelig1"
        tokenSet.refreshToken shouldBe "hemmelig2"
    }

    @Test
    fun `grant feiler`() {
        val client = createTestClient {
            respondJson(
                """
                {
                  "error": "invalid_scope",
                  "error_description": "AADSTS70011: The provided value for the input parameter 'scope' is not valid. The scope https://foo.microsoft.com/.default is not valid.\r\nTrace ID: 255d1aef-8c98-452f-ac51-23d051240864\r\nCorrelation ID: fb3d2015-bc17-4bb9-bb85-30c5cf1aaaa7\r\nTimestamp: 2016-01-09 02:02:12Z",
                  "error_codes": [
                    70011
                  ],
                  "timestamp": "2016-01-09 02:02:12Z",
                  "trace_id": "255d1aef-8c98-452f-ac51-23d051240864",
                  "correlation_id": "fb3d2015-bc17-4bb9-bb85-30c5cf1aaaa7"
                }
                """.trimIndent(), HttpStatusCode.BadRequest
            )
        }

        val error = assertThrows<OpenIDClientException> {
            runBlocking(Dispatchers.IO) {
                client.grant(scope = "invalid")
            }
        }

        println(error.message)
    }

    private fun createTestClient(handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData) =
        DefaultOpenIDClient(
            configuration = DefaultOpenIDConfiguration(
                tokenEndpoint = "https://issuer/token",
                clientId = "clientId",
                clientSecret = "clientSecret"
            ),
            engine = MockEngine {
                handler(this, it)
            }
        )
}
