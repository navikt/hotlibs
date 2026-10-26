package no.nav.hjelpemidler.server.ktor.security

import com.auth0.jwt.JWT
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.request.ApplicationRequest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.security.AuthenticatedApplication
import no.nav.hjelpemidler.server.ktor.security.test.applikasjon
import no.nav.hjelpemidler.server.ktor.security.test.applikasjonToken
import no.nav.hjelpemidler.server.ktor.security.test.lagAutentisertAnsatt
import org.junit.jupiter.api.Test

class DefaultEntraIdAuthenticatedPrincipalProviderTest {
    private val ansatt = lagAutentisertAnsatt()

    private val authenticatedPrincipalProvider = DefaultEntraIdAuthenticatedPrincipalProvider()

    private val request = mockk<ApplicationRequest>()
    private val call = mockk<ApplicationCall> {
        every { request } returns this@DefaultEntraIdAuthenticatedPrincipalProviderTest.request
    }

    @Test
    fun `Kaller DefaultEntraIdAuthenticatedPrincipalProvider for applikasjon`() = runTest {
        val credential = EntraIdCredential(JWTCredential(JWT.decode(applikasjonToken)))

        every { request.headers } returns headersOf(HttpHeaders.Authorization, "Bearer $applikasjonToken")

        val principal = authenticatedPrincipalProvider(call, credential)

        principal.shouldBeInstanceOf<AuthenticatedApplication>()
        principal.id shouldBe applikasjon.id
    }

    @Test
    fun `Kaller DefaultEntraIdAuthenticatedPrincipalProvider for ansatt`() = runTest {
        val credential = EntraIdCredential(JWTCredential(JWT.decode(ansatt.userToken)))

        every { request.headers } returns headersOf(HttpHeaders.Authorization, "Bearer ${ansatt.userToken}")

        val principal = authenticatedPrincipalProvider(call, credential)

        principal.shouldBeInstanceOf<AutentisertAnsatt>()
        principal.id shouldBe ansatt.id
        principal.navn shouldBe ansatt.navn
        principal.epost shouldBe ansatt.epost
        principal.grupper shouldBe ansatt.grupper
        principal.userToken shouldBe ansatt.userToken
    }
}
