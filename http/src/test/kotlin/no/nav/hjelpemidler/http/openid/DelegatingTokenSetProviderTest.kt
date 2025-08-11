package no.nav.hjelpemidler.http.openid

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.http.context.RequestContext
import no.nav.hjelpemidler.http.test.TestApplicationPrincipal
import no.nav.hjelpemidler.http.test.TestUserPrincipal
import org.junit.jupiter.api.Test

class DelegatingTokenSetProviderTest {
    private val client = mockk<TexasClient>(relaxed = true)
    private val identityProvider = IdentityProvider.ENTRA_ID
    private val defaultTarget = "test1"
    private val provider = DelegatingTokenSetProvider(client, identityProvider, defaultTarget)

    @Test
    fun `Gjør token exchange med userToken fra request`() = runTest {
        val userTokenFromContext = TestUserPrincipal.userToken
        val userTokenFromRequest = "userTokenFromRequest"

        withContext(RequestContext(TestUserPrincipal)) {
            provider(request { onBehalfOf(userTokenFromRequest) })
        }

        coVerify(exactly = 1) {
            client.exchange(identityProvider, defaultTarget, userTokenFromRequest)
        }
        coVerify(exactly = 0) {
            client.exchange(any(), any(), userTokenFromContext)
        }
        coVerify(exactly = 0) {
            client.token(any(), any())
        }
    }

    @Test
    fun `Gjør token exchange med userToken fra context`() = runTest {
        val userTokenFromContext = TestUserPrincipal.userToken

        withContext(RequestContext(TestUserPrincipal)) {
            provider(request())
        }

        coVerify(exactly = 1) {
            client.exchange(identityProvider, defaultTarget, userTokenFromContext)
        }
        coVerify(exactly = 0) {
            client.token(any(), any())
        }
    }

    @Test
    fun `Ikke gjør token exchange hvis request as application`() = runTest {
        withContext(RequestContext(TestUserPrincipal)) {
            provider(request { asApplication() })
        }

        coVerify(exactly = 1) {
            client.token(identityProvider, defaultTarget)
        }
        coVerify(exactly = 0) {
            client.exchange(any(), any(), any())
        }
    }

    @Test
    fun `Ikke gjør token exchange hvis principal er application`() = runTest {
        withContext(RequestContext(TestApplicationPrincipal)) {
            provider(request())
        }

        coVerify(exactly = 1) {
            client.token(identityProvider, defaultTarget)
        }
        coVerify(exactly = 0) {
            client.exchange(any(), any(), any())
        }
    }

    @Test
    fun `Overstyr target`() = runTest {
        val otherTarget = "test2"
        val userTokenFromContext = TestUserPrincipal.userToken

        withContext(RequestContext(TestUserPrincipal)) {
            provider(request { target(otherTarget) })
        }

        coVerify(exactly = 1) {
            client.exchange(identityProvider, otherTarget, userTokenFromContext)
        }
        coVerify(exactly = 0) {
            client.exchange(identityProvider, defaultTarget, userTokenFromContext)
        }
    }
}
