package no.nav.hjelpemidler.http.openid

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.Parameters
import io.ktor.http.content.OutgoingContent
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.http.test.respondJson
import kotlin.test.Test
import kotlin.time.Duration.Companion.hours

class TexasClientTest {
    private val target = Target(application = "test1").toString()
    private val identityProvider = IdentityProvider.ENTRA_ID

    @Test
    fun `Machine-To-Machine-token`() = runTest {
        val client = TexasClient(MockEngine {
            it.body.should { parameters ->
                parameters["identity_provider"] shouldBe identityProvider.toString()
                parameters["target"] shouldBe target
            }

            respondJson(
                TokenSet(
                    accessToken = "accessToken",
                    expiresIn = 1.hours,
                )
            )
        })

        val tokenSet = client.token(identityProvider, target)

        client.asOpenIDClient(identityProvider).grant(target) shouldBe tokenSet
    }

    @Test
    fun `On-Behalf-Of-token`() = runTest {
        val userToken = "userToken"
        val client = TexasClient(MockEngine {
            it.body.should { parameters ->
                parameters["identity_provider"] shouldBe identityProvider.toString()
                parameters["target"] shouldBe target
                parameters["user_token"] shouldBe userToken
            }

            respondJson(
                TokenSet(
                    accessToken = "accessToken",
                    expiresIn = 1.hours,
                )
            )
        })

        val tokenSet = client.exchange(identityProvider, target, userToken)

        client.asOpenIDClient(identityProvider).grant(target, userToken) shouldBe tokenSet
    }

    @Test
    fun `Valider token`() = runTest {
        val token = "accessToken"
        val client = TexasClient(MockEngine {
            it.body.should { parameters ->
                parameters["identity_provider"] shouldBe identityProvider.toString()
                parameters["token"] shouldBe token
            }
            respondJson(TokenIntrospection(active = true))
        })

        client.introspection(identityProvider, token).active shouldBe true
    }
}

private fun OutgoingContent.should(block: Parameters.(Parameters) -> Unit) {
    val content = shouldBeInstanceOf<FormDataContent>()
    assertSoftly(content.formData, block)
}
