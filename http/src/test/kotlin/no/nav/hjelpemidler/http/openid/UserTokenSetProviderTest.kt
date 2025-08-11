package no.nav.hjelpemidler.http.openid

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.http.context.RequestContext
import no.nav.hjelpemidler.http.test.TestUserPrincipal
import org.junit.jupiter.api.Test

class UserTokenSetProviderTest {
    private val client = mockk<TexasClient>(relaxed = true)
    private val identityProvider = IdentityProvider.ENTRA_ID
    private val defaultTarget = Target(application = "test1")
    private val provider = UserTokenSetProvider(client, identityProvider, defaultTarget)

    @Test
    fun `Gjør token exchange med userToken i request`() = runTest {
        val userTokenFromRequest = "userTokenFromRequest"

        provider(request { onBehalfOf(userTokenFromRequest) })

        coVerify(exactly = 1) {
            client.exchange(identityProvider, defaultTarget.toString(), userTokenFromRequest)
        }
        coVerify(exactly = 0) {
            client.token(any(), any())
        }
    }

    @Test
    fun `Gjør token exchange med userToken i context`() = runTest {
        val userTokenFromContext = TestUserPrincipal.userToken.toString()

        withContext(RequestContext(TestUserPrincipal)) {
            provider(request())
        }

        coVerify(exactly = 1) {
            client.exchange(identityProvider, defaultTarget.toString(), userTokenFromContext)
        }
        coVerify(exactly = 0) {
            client.token(any(), any())
        }
    }

    @Test
    fun `Gjør token exchange uten userToken`() = runTest {
        shouldThrow<IllegalStateException> {
            provider(request())
        }.shouldHaveMessage("userToken mangler")
    }

    @Test
    fun `Overstyrer target`() = runTest {
        val otherTarget = Target(application = "test2")
        val userTokenFromRequest = "userTokenFromRequest"

        provider(request {
            target(otherTarget)
            onBehalfOf(userTokenFromRequest)
        })

        coVerify(exactly = 1) {
            client.exchange(identityProvider, otherTarget.toString(), userTokenFromRequest)
        }
    }
}
