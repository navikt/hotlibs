package no.nav.hjelpemidler.server.ktor.security

import com.auth0.jwt.exceptions.IncorrectClaimException
import no.nav.hjelpemidler.server.ktor.security.test.asToken
import no.nav.hjelpemidler.server.ktor.security.test.lagAutentisertInnbygger
import no.nav.hjelpemidler.server.ktor.security.test.verifyTokenXToken
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class TokenXCredentialTest {
    @Test
    fun `Krever level HIGH og har det`() {
        val innbygger = lagAutentisertInnbygger(level = IdPortenSecurityLevel.HIGH)
        assertDoesNotThrow {
            verifyTokenXToken(innbygger.asToken()) {
                withMinimumSecurityLevel(IdPortenSecurityLevel.HIGH)
            }
        }
    }

    @Test
    fun `Krever level SUBSTANTIAL og har det`() {
        val innbygger = lagAutentisertInnbygger(level = IdPortenSecurityLevel.SUBSTANTIAL)
        assertDoesNotThrow {
            verifyTokenXToken(innbygger.asToken()) {
                withMinimumSecurityLevel(IdPortenSecurityLevel.SUBSTANTIAL)
            }
        }
    }

    @Test
    fun `Krever level HIGH, men har SUBSTANTIAL`() {
        val innbygger = lagAutentisertInnbygger(level = IdPortenSecurityLevel.SUBSTANTIAL)
        assertThrows<IncorrectClaimException>(message = "The Claim 'acr' value doesn't match the required one.") {
            verifyTokenXToken(innbygger.asToken()) {
                withMinimumSecurityLevel(IdPortenSecurityLevel.HIGH)
            }
        }
    }
}
