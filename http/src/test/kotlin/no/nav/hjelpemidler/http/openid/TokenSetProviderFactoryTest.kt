package no.nav.hjelpemidler.http.openid

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Nested
import kotlin.test.Test

class TokenSetProviderFactoryTest {
    private val client = mockk<TexasClient>(relaxed = true)
    private val identityProvider = IdentityProvider.ENTRA_ID
    private val defaultTarget = "test1"
    private val factory = TokenSetProviderFactory(client)

    private val userOpenIDContext = OpenIDContext(false, "userTokenFromContext")
    private val applicationOpenIDContext = OpenIDContext(true, null)

    @Nested
    inner class Application {
        private val provider = factory.application(identityProvider, defaultTarget)

        @Test
        fun `Henter token`() = runTest {
            provider(request())

            coVerify(exactly = 1) {
                client.token(identityProvider, defaultTarget)
            }
            coVerify(exactly = 0) {
                client.exchange(any(), any(), any())
            }
        }

        @Test
        fun `Overstyrer target`() = runTest {
            val otherTarget = "test2"

            provider(request { target(otherTarget) })

            coVerify(exactly = 1) {
                client.token(identityProvider, otherTarget)
            }
            coVerify(exactly = 0) {
                client.token(identityProvider, defaultTarget)
            }
        }
    }

    @Nested
    inner class User {
        private val provider = factory.user(identityProvider, defaultTarget)

        @Test
        fun `Gjør token exchange med userToken i request`() = runTest {
            val userTokenFromRequest = "userTokenFromRequest"

            provider(request {
                onBehalfOf(userTokenFromRequest)
            })

            coVerify(exactly = 1) {
                client.exchange(identityProvider, defaultTarget, userTokenFromRequest)
            }
            coVerify(exactly = 0) {
                client.token(any(), any())
            }
        }

        @Test
        fun `Gjør token exchange med userToken i context`() = runTest {
            val userTokenFromContext = userOpenIDContext.userToken.shouldNotBeNull()

            withContext(userOpenIDContext) {
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
                client.exchange(identityProvider, otherTarget, userTokenFromRequest)
            }
            coVerify(exactly = 0) {
                client.exchange(identityProvider, defaultTarget, userTokenFromRequest)
            }
        }
    }

    @Nested
    inner class Delegate {
        private val provider = factory.delegate(identityProvider, defaultTarget)

        @Test
        fun `Gjør token exchange med userToken fra request`() = runTest {
            val userTokenFromContext = userOpenIDContext.userToken.shouldNotBeNull()
            val userTokenFromRequest = "userTokenFromRequest"

            withContext(userOpenIDContext) {
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
            val userTokenFromContext = userOpenIDContext.userToken.shouldNotBeNull()

            withContext(userOpenIDContext) {
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
            withContext(userOpenIDContext) {
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
            withContext(applicationOpenIDContext) {
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
            val userTokenFromContext = userOpenIDContext.userToken.shouldNotBeNull()

            withContext(userOpenIDContext) {
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
}
