package no.nav.hjelpemidler.nare.policy

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import no.nav.hjelpemidler.nare.test.harResultat
import kotlin.test.Test

class PolicyTest {
    private val tilgangTilEnhet = Policy<TestPolicyContext>("Har tilgang til enhet", "tilgangTilEnhet") {
        Policyavgjørelse.TILLAT(begrunnelse = "Innlogget ansatt har tilgang til enhet")
    }
    private val tilgangTilPerson = Policy<TestPolicyContext>("Har tilgang til person", "tilgangTilPerson") {
        Policyavgjørelse.NEKT(begrunnelse = "Innlogget ansatt har ikke tilgang til person")
    }

    @Test
    fun `Policy har forventet resultat`() {
        tilgangTilEnhet.evaluer(TestPolicyContext) harResultat Policyavgjørelse.TILLAT
        tilgangTilPerson.evaluer(TestPolicyContext) harResultat Policyavgjørelse.NEKT

        tilgangTilEnhet.harTillatelse(TestPolicyContext).shouldBeTrue()
        tilgangTilPerson.harTillatelse(TestPolicyContext).shouldBeFalse()

        tilgangTilEnhet.krevTillatelse(TestPolicyContext) {}.shouldBeSuccess()
        tilgangTilPerson.krevTillatelse(TestPolicyContext) {}.shouldBeFailure()

        TestPolicyContext.kan(tilgangTilEnhet) harResultat Policyavgjørelse.TILLAT
        TestPolicyContext.kan(tilgangTilPerson) harResultat Policyavgjørelse.NEKT

        TestPolicyContext.kanIkke(tilgangTilEnhet) harResultat Policyavgjørelse.NEKT
        TestPolicyContext.kanIkke(tilgangTilPerson) harResultat Policyavgjørelse.TILLAT
    }

    private val tilgangTilSak = (tilgangTilEnhet og tilgangTilPerson)
        .med("Har tilgang til sak", "tilgangTilSak")
    private val tilgangTilOppgave = (tilgangTilEnhet eller tilgangTilPerson)
        .med("Har tilgang til oppgave", "tilgangTilOppgave")

    @Test
    fun `Kombinert policy har forventet resultat`() {
        val policy = tilgangTilSak og tilgangTilOppgave
        val policyevaluering = policy.evaluer(TestPolicyContext)
        policyevaluering harResultat Policyavgjørelse.NEKT
    }
}

private data object TestPolicyContext : PolicyContext
