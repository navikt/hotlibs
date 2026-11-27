package no.nav.hjelpemidler.server.ktor.security

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.ktor.http.auth.HttpAuthHeader
import io.mockk.called
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import no.nav.hjelpemidler.collections.enumSetOf
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import no.nav.hjelpemidler.http.openid.IdentityProvider
import no.nav.hjelpemidler.http.openid.Texas
import no.nav.hjelpemidler.http.openid.TokenIntrospection
import no.nav.hjelpemidler.server.ktor.security.test.applikasjonToken
import no.nav.hjelpemidler.server.ktor.security.test.lagAutentisertInnbygger
import org.junit.jupiter.api.Test
import kotlin.io.encoding.Base64

class TexasVerifierTest {
    private val texas: Texas = mockk()
    private val identityProviders = enumSetOf(IdentityProvider.ENTRA_ID, IdentityProvider.TOKEN_X)

    @Test
    fun `Ikke bearer token`() = runTest {
        val verifier = TexasVerifier(texas, identityProviders)
        val token = HttpAuthHeader.Single("Basic", Base64.encode("username:password"))

        verifier(token) shouldBe null

        verify { texas wasNot called }
    }

    @Test
    fun `Svarer med IntrospectedTokenVerifier for aktivt applikasjon-token`() = runTest {
        val verifier = TexasVerifier(texas, identityProviders)
        val token = HttpAuthHeader.Single("Bearer", applikasjonToken)

        coEvery { texas.introspection(IdentityProvider.ENTRA_ID, token.blob) } returns TokenIntrospection(active = true)

        verifier(token).shouldBeInstanceOf<IntrospectedTokenVerifier>()
    }

    @Test
    fun `Svarer med IntrospectedTokenVerifier for aktivt innbygger-token`() = runTest {
        val verifier = TexasVerifier(texas, identityProviders)
        val token = HttpAuthHeader.Single("Bearer", lagAutentisertInnbygger(Fødselsnummer(50.år)).userToken)

        coEvery { texas.introspection(IdentityProvider.TOKEN_X, token.blob) } returns TokenIntrospection(active = true)

        verifier(token).shouldBeInstanceOf<IntrospectedTokenVerifier>()
    }
}

private fun Base64.encode(source: String): String = encode(source.toByteArray())
