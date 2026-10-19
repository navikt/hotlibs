package no.nav.hjelpemidler.http.openid

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.security.AuthenticationContext
import no.nav.hjelpemidler.security.TestAuthenticatedUser
import org.junit.jupiter.api.Nested
import kotlin.test.Test

class TexasTokenSetProviderTest {
    private val texas = mockk<Texas>(relaxed = true)
    private val defaultTarget = "test1"

    private val userPrincipalContext = AuthenticationContext(TestAuthenticatedUser)

    @Nested
    inner class Application {
        private val identityProvider = IdentityProvider.ENTRA_ID
        private val provider = ApplicationTokenSetProvider(texas, identityProvider, defaultTarget)

        @Test
        fun `Henter token`() = runTest {
            provider(request())

            coVerify(exactly = 1) {
                texas.token(identityProvider, defaultTarget)
            }
            coVerify(exactly = 0) {
                texas.exchange(any(), any(), any())
            }
        }

        @Test
        fun `Overstyrer target`() = runTest {
            val otherTarget = "test2"

            provider(request { target(otherTarget) })

            coVerify(exactly = 1) {
                texas.token(identityProvider, otherTarget)
            }
            coVerify(exactly = 0) {
                texas.token(identityProvider, defaultTarget)
            }
        }
    }

    @Nested
    inner class User {
        private val identityProvider = IdentityProvider.TOKEN_X
        private val provider = UserTokenSetProvider(texas, identityProvider, defaultTarget)

        @Test
        fun `Gjør token exchange med userToken i request`() = runTest {
            val userTokenFromRequest = "userTokenFromRequest"

            provider(request {
                onBehalfOf(userTokenFromRequest)
            })

            coVerify(exactly = 1) {
                texas.exchange(identityProvider, defaultTarget, userTokenFromRequest)
            }
            coVerify(exactly = 0) {
                texas.token(any(), any())
            }
        }

        @Test
        fun `Gjør token exchange med userToken i context`() = runTest {
            val userTokenFromContext = userPrincipalContext.principal.userToken.shouldNotBeNull()

            withContext(userPrincipalContext) {
                provider(request())
            }

            coVerify(exactly = 1) {
                texas.exchange(identityProvider, defaultTarget, userTokenFromContext)
            }
            coVerify(exactly = 0) {
                texas.token(any(), any())
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
            val otherTarget = "test2"
            val userTokenFromRequest = "userTokenFromRequest"

            provider(request {
                target(otherTarget)
                onBehalfOf(userTokenFromRequest)
            })

            coVerify(exactly = 1) {
                texas.exchange(identityProvider, otherTarget, userTokenFromRequest)
            }
            coVerify(exactly = 0) {
                texas.exchange(identityProvider, defaultTarget, userTokenFromRequest)
            }
        }
    }
}
