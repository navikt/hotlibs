package no.nav.hjelpemidler.http.openid

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import kotlinx.coroutines.runBlocking
import no.nav.hjelpemidler.http.test.notSameAs
import no.nav.hjelpemidler.http.test.respondJson
import no.nav.hjelpemidler.http.test.sameAs
import kotlin.test.Test
import kotlin.time.Duration.Companion.minutes

class CachedOpenIDClientTest {
    @Test
    fun `skal bruke token fra cache`() {
        val tokenSet = TokenSet.bearer(expiresIn = 10.minutes, "token")
        val client = createTestClient {
            respondJson(tokenSet)
        }
        runBlocking {
            val tokenSet1 = client.grant("test", "s1")
            val tokenSet2 = client.grant("test", "s1")
            tokenSet1 sameAs tokenSet2
        }
    }

    @Test
    fun `skal ikke bruke token fra cache, ulike parametre`() {
        val tokenSet = TokenSet.bearer(expiresIn = 10.minutes, "token")
        val client = createTestClient {
            respondJson(tokenSet)
        }
        runBlocking {
            val tokenSet1 = client.grant("test", "s1")
            val tokenSet2 = client.grant("test", "s2")
            tokenSet1 notSameAs tokenSet2
        }
    }

    @Test
    fun `skal ikke bruke token fra cache, token utløpt`() {
        val tokenSet = TokenSet.bearer(expiresIn = 0.minutes, "token")
        val client = createTestClient {
            respondJson(tokenSet)
        }
        runBlocking {
            val tokenSet1 = client.grant("test", "s1")
            val tokenSet2 = client.grant("test", "s1")
            tokenSet1 notSameAs tokenSet2
        }
    }

    private fun createTestClient(handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData): OpenIDClient =
        createOpenIDClient(
            configuration = DefaultOpenIDConfiguration(
                tokenEndpoint = "https://issuer/token",
                clientId = "clientId",
                clientSecret = "clientSecret"
            ),
            engine = MockEngine {
                handler(this, it)
            }
        ) {
            tokenExpiry()
        }
}
