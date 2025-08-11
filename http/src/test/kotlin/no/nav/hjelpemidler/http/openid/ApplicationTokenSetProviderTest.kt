package no.nav.hjelpemidler.http.openid

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class ApplicationTokenSetProviderTest {
    private val client = mockk<TexasClient>(relaxed = true)
    private val identityProvider = IdentityProvider.ENTRA_ID
    private val defaultTarget = "test1"
    private val provider = ApplicationTokenSetProvider(client, identityProvider, defaultTarget)

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
