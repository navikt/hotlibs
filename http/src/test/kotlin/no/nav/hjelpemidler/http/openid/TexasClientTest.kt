package no.nav.hjelpemidler.http.openid

import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.http.test.respondJson
import kotlin.test.Test
import kotlin.time.Duration.Companion.hours

class TexasClientTest {
    private val client = TexasClient(MockEngine {
        when (it.url.encodedPath) {
            "/api/v1/token" -> respondJson(
                TokenSet(
                    accessToken = "accessToken",
                    expiresIn = 1.hours,
                )
            )

            "/api/v1/token/exchange" -> respondJson(
                TokenSet(
                    accessToken = "accessToken",
                    expiresIn = 1.hours,
                )
            )

            "/api/v1/introspect" -> respondJson(TokenIntrospection(active = true))
            else -> error("Unhandled: ${it.url}")
        }
    })

    private val target = scopeOf("test")
    private val identityProvider = IdentityProvider.ENTRA_ID

    private val openIDClient = client.asOpenIDClient(identityProvider)

    @Test
    fun `M2M token`() = runTest {
        val tokenSet = client.token(identityProvider, target)
        openIDClient.grant(target) shouldBe tokenSet
    }

    @Test
    fun `OBO token`() = runTest {
        val userToken = "userToken"
        val tokenSet = client.exchange(identityProvider, target, userToken)
        openIDClient.grant(target, userToken) shouldBe tokenSet
    }

    @Test
    fun `Valider token`() = runTest {
        client.introspection(identityProvider, "accessToken").active shouldBe true
    }
}
