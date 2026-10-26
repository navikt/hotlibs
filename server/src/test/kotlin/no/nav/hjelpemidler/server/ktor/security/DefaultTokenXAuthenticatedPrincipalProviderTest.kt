package no.nav.hjelpemidler.server.ktor.security

import com.auth0.jwt.JWT
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTCredential
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.server.ktor.security.test.lagAutentisertInnbygger
import org.junit.jupiter.api.Test

class DefaultTokenXAuthenticatedPrincipalProviderTest {
    private val innbygger = lagAutentisertInnbygger()

    private val authenticatedPrincipalProvider = DefaultTokenXAuthenticatedPrincipalProvider()

    private val call = mockk<ApplicationCall> {
        every { request } returns mockk {
            every { headers } returns headersOf(HttpHeaders.Authorization, "Bearer ${innbygger.userToken}")
        }
    }

    @Test
    fun `Kaller DefaultTokenXAuthenticatedPrincipalProvider`() = runTest {
        val credential = TokenXCredential(JWTCredential(JWT.decode(innbygger.userToken)))

        val principal = authenticatedPrincipalProvider(call, credential)

        principal.shouldBeInstanceOf<AutentisertInnbygger>()
        principal.id shouldBe innbygger.id
        principal.fnr shouldBe innbygger.fnr
        principal.level shouldBe innbygger.level
        principal.userToken shouldBe innbygger.userToken
    }
}
