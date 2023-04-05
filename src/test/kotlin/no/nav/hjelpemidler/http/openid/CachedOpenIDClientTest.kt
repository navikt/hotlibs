package no.nav.hjelpemidler.http.openid

import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.http.test.respondJson
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes

class CachedOpenIDClientTest {
    @Test
    fun `skal bruke token fra cache`() = runTest {
        val tokenSet = TokenSet.bearer(expiresIn = 10.minutes, "token")
        val client = createTestClient {
            respondJson(tokenSet)
        }

        val tokenSet1 = client.grant("test", "s1")
        val tokenSet2 = client.grant("test", "s1")

        tokenSet1 shouldBeSameInstanceAs tokenSet2
    }

    @Test
    fun `skal ikke bruke token fra cache, ulike parametre`() = runTest {
        val tokenSet = TokenSet.bearer(expiresIn = 10.minutes, "token")
        val client = createTestClient {
            respondJson(tokenSet)
        }

        val tokenSet1 = client.grant("test", "s1")
        val tokenSet2 = client.grant("test", "s2")

        tokenSet1 shouldNotBeSameInstanceAs tokenSet2
    }

    @Test
    fun `skal ikke bruke token fra cache, token utløpt`() = runTest {
        val tokenSet = TokenSet.bearer(expiresIn = 0.minutes, "token")
        val client = createTestClient {
            respondJson(tokenSet)
        }

        val tokenSet1 = client.grant("test", "s1")
        val tokenSet2 = client.grant("test", "s1")

        tokenSet1 shouldNotBeSameInstanceAs tokenSet2
    }

    private fun createTestClient(
        handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData,
    ): OpenIDClient =
        createOpenIDClient(
            engine = MockEngine {
                handler(this, it)
            },
        ) {
            tokenEndpoint = "https://issuer/token"
            clientId = "clientId"
            clientSecret = "clientSecret"

            cache {
                maximumSize = 10
            }
        }.also {
            assertTrue {
                it is CachedOpenIDClient
            }
        }
}
