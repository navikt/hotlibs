package no.nav.hjelpemidler.nare.regel

import no.nav.hjelpemidler.nare.test.harResultat
import kotlin.test.Test

class RegelTest {
    private val r1 = Regel<String>("r1", lovreferanse = Lovreferanse("$1", "http://lovdata/1")) {
        ja("JA")
    }
    private val r2 = Regel<String>("r2", lovreferanse = Lovreferanse("$2", "http://lovdata/2")) {
        nei("NEI")
    }
    private val r3 = r1 og r2
    private val r4 = r1 eller r2

    @Test
    fun `Regel har forventet resultat`() {
        r1.evaluer("") harResultat Regelutfall.JA
        r2.evaluer("") harResultat Regelutfall.NEI

        r1.ikke().evaluer("") harResultat Regelutfall.NEI
        r2.ikke().evaluer("") harResultat Regelutfall.JA

        r3.evaluer("") harResultat Regelutfall.NEI
        r4.evaluer("") harResultat Regelutfall.JA
    }
}
