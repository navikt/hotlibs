package no.nav.hjelpemidler.security

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.hjelpemidler.domain.tilgang.Applikasjonsnavn
import kotlin.test.Test

class AuthenticatedPrincipalTest {
    private val applikasjonsnavn = Applikasjonsnavn("testApplication")

    @Test
    fun `AuthenticatedPrincipal er applikasjon`() {
        val principal: AuthenticatedPrincipal = AutentisertApplikasjon(applikasjonsnavn)

        principal.isApplikasjon.shouldBeTrue()
        principal.isPerson.shouldBeFalse()

        // smart cast
        if (principal.isApplikasjon) {
            principal.id.shouldBeInstanceOf<Applikasjonsnavn>()
        }

        principal.requireApplikasjon() shouldBe principal
        shouldThrow<IllegalStateException> { principal.requirePerson() }

        principal.mapApplikasjonOrNull { it.id } shouldBe principal.id
        principal.mapPersonOrNull { it.userToken } shouldBe null
    }

    @Test
    fun `AuthenticatedPrincipal er person`() {
        val principal: AuthenticatedPrincipal = TestAutentisertAnsatt

        principal.isPerson.shouldBeTrue()
        principal.isApplikasjon.shouldBeFalse()

        // smart cast
        if (principal.isPerson) {
            principal.userToken.shouldNotBeBlank()
        }

        principal.requirePerson() shouldBe principal
        shouldThrow<IllegalStateException> { principal.requireApplikasjon() }

        principal.mapPersonOrNull { it.userToken } shouldBe TestAutentisertAnsatt.userToken
        principal.mapApplikasjonOrNull { it.id } shouldBe null
    }
}
