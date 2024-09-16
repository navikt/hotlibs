package no.nav.hjelpemidler.http.openid

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.http.test.respondJson
import kotlin.test.Test
import kotlin.test.assertTrue

class DefaultOpenIDClientTest {
    @Test
    fun `Client credentials grant`() = runTest {
        val client = createTestClient {
            respondJson(
                """
                {
                  "token_type": "Bearer",
                  "expires_in": 3599,
                  "access_token": "token"
                }
                """.trimIndent()
            )
        }

        val tokenSet = client.grant(scope = "test")

        tokenSet.tokenType shouldBe "Bearer"
        tokenSet.expiresIn shouldBe 3599
        tokenSet.accessToken shouldBe "token"
    }

    @Test
    fun `On behalf of grant`() = runTest {
        val client = createTestClient {
            respondJson(
                """
                {
                  "token_type": "Bearer",
                  "scope": "https://graph.microsoft.com/user.read",
                  "expires_in": 3269,
                  "ext_expires_in": 0,
                  "access_token": "token"
                }
                """.trimIndent()
            )
        }

        val tokenSet = client.grant(scope = "test", onBehalfOf = "test")

        tokenSet.tokenType shouldBe "Bearer"
        tokenSet.expiresIn shouldBe 3269
        tokenSet.accessToken shouldBe "token"
    }

    @Test
    fun `Grant feiler`() = runTest {
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

        shouldThrow<OpenIDClientException> {
            client.grant(scope = "invalid")
        }
    }

    private fun createTestClient(
        handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData,
    ): OpenIDClient =
        createOpenIDClient(
            engine = MockEngine {
                handler(this, it)
            }
        ) {
            tokenEndpoint = "https://issuer/token"
            clientId = "clientId"
            clientSecret = "clientSecret"
        }.also {
            assertTrue {
                it is DefaultOpenIDClient
            }
        }
}
