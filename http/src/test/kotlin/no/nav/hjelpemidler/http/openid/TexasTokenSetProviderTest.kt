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
    private val target = Target(application = "hotlibs").toString()
    private val provider = TexasTokenSetProvider(client, identityProvider, Target(target))

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
            provider(HttpRequestBuilder().apply { påVegneAv(userTokenFromRequest) })
        }

        coVerify(exactly = 0) {
            client.token(identityProvider, target)
        }
        coVerify(exactly = 0) {
            client.exchange(identityProvider, target, userTokenFromContext)
        }
    }

    @Test
    fun `Gjør ikke token exchange hvis SomSystembruker er satt`() = runTest {
        val userToken = "userTokenFromContext"

        coEvery {
            client.token(identityProvider, target)
        } returns TokenSet("accessToken", 1.hours)

        withUserContext(userToken) {
            provider(HttpRequestBuilder().apply { somSystembruker() })
        }

        coVerify(exactly = 0) {
            client.exchange(identityProvider, target, userToken)
        }
    }

    @Test
    fun `Overstyr target`() = runTest {
        val otherTarget = Target(application = "hotsak").toString()
        val userToken = "userTokenFromContext"

        coEvery {
            client.exchange(identityProvider, otherTarget, userToken)
        } returns TokenSet("accessToken", 1.hours)

        withUserContext(userToken) {
            provider(HttpRequestBuilder().apply { target(otherTarget) })
        }

        coVerify(exactly = 0) {
            client.exchange(identityProvider, target, userToken)
        }
    }
}
