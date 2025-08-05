package no.nav.hjelpemidler.http.openid

import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondError
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.http.createHttpClient
import no.nav.hjelpemidler.http.test.respondJson
import kotlin.test.Test
import kotlin.time.Duration.Companion.hours

class OpenIDPluginTest {
    @Test
    fun `Skal hente og bruke access token fra klient`() = runTest {
        val engine = MockEngine {
            when {
                it.url.toString().endsWith("/token") ->
                    respondJson(
                        TokenSet(
                            accessToken = "accessToken",
                            expiresIn = 1.hours,
                        )
                    )

                it.url.toString().endsWith("/test") -> {
                    it.headers["Authorization"] shouldBe "Bearer accessToken"
                    respondOk()
                }

                else ->
                    respondError(HttpStatusCode.NotFound)
            }
        }
        val client = createHttpClient(engine) {
            openID(IdentityProvider.ENTRA_ID, "test")
        }

        val response = client.get("/test")

        response.status shouldBe HttpStatusCode.OK
    }
}
