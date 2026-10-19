package no.nav.hjelpemidler.http.openid

import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import no.nav.hjelpemidler.domain.tilgang.Applikasjonsnavn
import no.nav.hjelpemidler.security.AuthenticatedApplication
import no.nav.hjelpemidler.security.AuthenticationContext
import no.nav.hjelpemidler.security.TestAuthenticatedUser
import kotlin.test.Test

class EntraIDTokenSetProviderTest {
    private val applikasjonsnavn = Applikasjonsnavn("testApplication")

    private val texas = mockk<Texas>(relaxed = true)
    private val identityProvider = IdentityProvider.ENTRA_ID
    private val defaultTarget = "test1"

    private val applicationPrincipalContext = AuthenticationContext(AuthenticatedApplication(applikasjonsnavn))
    private val userPrincipalContext = AuthenticationContext(TestAuthenticatedUser)

    private val provider = EntraIDTokenSetProvider(texas, defaultTarget)

    @Test
    fun `Gjør token exchange med userToken fra request`() = runTest {
        val userTokenFromContext = userPrincipalContext.principal.userToken.shouldNotBeNull()
        val userTokenFromRequest = "userTokenFromRequest"

        withContext(userPrincipalContext) {
            provider(request { onBehalfOf(userTokenFromRequest) })
        }

        coVerify(exactly = 1) {
            texas.exchange(identityProvider, defaultTarget, userTokenFromRequest)
        }
        coVerify(exactly = 0) {
            texas.exchange(any(), any(), userTokenFromContext)
        }
        coVerify(exactly = 0) {
            texas.token(any(), any())
        }
    }

    @Test
    fun `Gjør token exchange med userToken fra context`() = runTest {
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
    fun `Ikke gjør token exchange hvis request as application`() = runTest {
        withContext(userPrincipalContext) {
            provider(request { asApplication() })
        }

        coVerify(exactly = 1) {
            texas.token(identityProvider, defaultTarget)
        }
        coVerify(exactly = 0) {
            texas.exchange(any(), any(), any())
        }
    }

    @Test
    fun `Ikke gjør token exchange hvis principal er application`() = runTest {
        withContext(applicationPrincipalContext) {
            provider(request())
        }

        coVerify(exactly = 1) {
            texas.token(identityProvider, defaultTarget)
        }
        coVerify(exactly = 0) {
            texas.exchange(any(), any(), any())
        }
    }

    @Test
    fun `Overstyr target`() = runTest {
        val otherTarget = "test2"
        val userTokenFromContext = userPrincipalContext.principal.userToken.shouldNotBeNull()

        withContext(userPrincipalContext) {
            provider(request { target(otherTarget) })
        }

        coVerify(exactly = 1) {
            texas.exchange(identityProvider, otherTarget, userTokenFromContext)
        }
        coVerify(exactly = 0) {
            texas.exchange(identityProvider, defaultTarget, userTokenFromContext)
        }
    }
}
