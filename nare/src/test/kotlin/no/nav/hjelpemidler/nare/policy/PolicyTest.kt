package no.nav.hjelpemidler.nare.policy

import no.nav.hjelpemidler.nare.test.harResultat
import org.junit.jupiter.api.Test

class PolicyTest {
    private val tillat = Policy<String>("tillat", "tillat") { Policyevaluering.tillat("true") }
    private val nekt = Policy<String>("nekt", "nekt") { Policyevaluering.nekt("false") }
    private val tillatOgNekt = (tillat og nekt).med("tillat_og_nekt", "tillat_og_nekt")
    private val tillatEllerNekt = (tillat eller nekt).med("tillat_eller_nekt", "tillat_eller_nekt")

    @Test
    fun `Policy har forventet resultat`() {
        tillat.evaluer("") harResultat Policyavgjørelse.TILLAT
        nekt.evaluer("") harResultat Policyavgjørelse.NEKT
    }
}
