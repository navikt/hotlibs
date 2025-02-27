package no.nav.hjelpemidler.nare.policy

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import no.nav.hjelpemidler.nare.test.harResultat
import org.junit.jupiter.api.Test

class PolicyTest {
    private val tillat = Policy<TestPolicyContext>("tillat", "tillat") { Policyevaluering.tillat("true") }
    private val nekt = Policy<TestPolicyContext>("nekt", "nekt") { Policyevaluering.nekt("false") }
    // private val tillatOgNekt = (tillat og nekt).med("tillat_og_nekt", "tillat_og_nekt")
    // private val tillatEllerNekt = (tillat eller nekt).med("tillat_eller_nekt", "tillat_eller_nekt")

    @Test
    fun `Policy har forventet resultat`() {
        tillat.evaluer(TestPolicyContext) harResultat Policyavgjørelse.TILLAT
        nekt.evaluer(TestPolicyContext) harResultat Policyavgjørelse.NEKT

        tillat.harTillatelse(TestPolicyContext).shouldBeTrue()
        nekt.harTillatelse(TestPolicyContext).shouldBeFalse()

        tillat.krevTillatelse(TestPolicyContext) {}.shouldBeSuccess()
        nekt.krevTillatelse(TestPolicyContext) {}.shouldBeFailure()

        TestPolicyContext.kan(tillat) harResultat Policyavgjørelse.TILLAT
        TestPolicyContext.kan(nekt) harResultat Policyavgjørelse.NEKT

        TestPolicyContext.kanIkke(tillat) harResultat Policyavgjørelse.NEKT
        TestPolicyContext.kanIkke(nekt) harResultat Policyavgjørelse.TILLAT
    }
}

private data object TestPolicyContext : PolicyContext
