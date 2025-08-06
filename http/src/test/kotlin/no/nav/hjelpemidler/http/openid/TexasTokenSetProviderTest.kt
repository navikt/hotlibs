package no.nav.hjelpemidler.http.openid

import io.ktor.client.request.HttpRequestBuilder
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.hours

class TexasTokenSetProviderTest {
    private val client = mockk<TexasClient>()
    private val identityProvider = IdentityProvider.ENTRA_ID
    private val target = "test"
    private val provider = TexasTokenSetProvider(client, identityProvider, target)

    @Test
    fun `Gjør token exchange med userToken fra context`() = runTest {
        val userToken = "userTokenFromContext"

        coEvery {
            client.exchange(identityProvider, target, userToken)
        } returns TokenSet("accessToken", 1.hours)

        withUserContext(userToken) {
            provider(HttpRequestBuilder())
        }

        coVerify(exactly = 0) {
            client.token(identityProvider, target)
        }
    }

    @Test
    fun `Gjør token exchange med userToken fra request`() = runTest {
        val userTokenFromContext = "userTokenFromContext"
        val userTokenFromRequest = "userTokenFromRequest"

        coEvery {
            client.exchange(identityProvider, target, userTokenFromRequest)
        } returns TokenSet("accessToken", 1.hours)

        withUserContext(userTokenFromContext) {
            provider(HttpRequestBuilder().apply { userToken(userTokenFromRequest) })
        }

        coVerify(exactly = 0) {
            client.token(identityProvider, target)
        }
        coVerify(exactly = 0) {
            client.exchange(identityProvider, target, userTokenFromContext)
        }
    }

    @Test
    fun `Gjør ikke token exchange hvis TokenExchangePreventionToken er satt`() = runTest {
        val userToken = "userTokenFromContext"

        coEvery {
            client.token(identityProvider, target)
        } returns TokenSet("accessToken", 1.hours)

        withUserContext(userToken) {
            provider(HttpRequestBuilder().apply { preventTokenExchange() })
        }

        coVerify(exactly = 0) {
            client.exchange(identityProvider, target, userToken)
        }
    }
}
