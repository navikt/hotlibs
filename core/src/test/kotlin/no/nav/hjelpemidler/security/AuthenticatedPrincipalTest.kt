package no.nav.hjelpemidler.security

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.hjelpemidler.domain.tilgang.Applikasjonsnavn
import no.nav.hjelpemidler.domain.tilgang.applikasjonsnavn
import kotlin.test.Test

class AuthenticatedPrincipalTest {
    @Test
    fun `AuthenticatedPrincipal er applikasjon`() {
        val principal: AuthenticatedPrincipal = AuthenticatedApplication(applikasjonsnavn)

        principal.isApplication.shouldBeTrue()
        principal.isUser.shouldBeFalse()

        // smart cast
        if (principal.isApplication) {
            principal.id.shouldBeInstanceOf<Applikasjonsnavn>()
        }

        principal.requireApplication() shouldBe principal
        shouldThrow<IllegalStateException> { principal.requireUser() }

        principal.mapApplicationOrNull { it.id } shouldBe principal.id
        principal.mapUserOrNull { it.userToken } shouldBe null
    }

    @Test
    fun `AuthenticatedPrincipal er bruker`() {
        val principal: AuthenticatedPrincipal = TestAuthenticatedUser

        principal.isUser.shouldBeTrue()
        principal.isApplication.shouldBeFalse()

        // smart cast
        if (principal.isUser) {
            principal.userToken.shouldNotBeBlank()
        }

        principal.requireUser() shouldBe principal
        shouldThrow<IllegalStateException> { principal.requireApplication() }

        principal.mapUserOrNull { it.userToken } shouldBe TestAuthenticatedUser.userToken
        principal.mapApplicationOrNull { it.id } shouldBe null
    }
}
