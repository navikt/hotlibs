package no.nav.hjelpemidler.nare.policy

import no.nav.hjelpemidler.nare.core.LogiskOperand

enum class PolicyResultat : LogiskOperand<PolicyResultat> {
    TILLAT {
        override infix fun og(annen: PolicyResultat): PolicyResultat = annen
        override infix fun eller(annen: PolicyResultat): PolicyResultat = TILLAT
        override fun ikke(): PolicyResultat = NEKT
    },

    NEKT {
        override infix fun og(annen: PolicyResultat): PolicyResultat = NEKT
        override infix fun eller(annen: PolicyResultat): PolicyResultat = annen
        override fun ikke(): PolicyResultat = TILLAT
    },

    IKKE_AKTUELT {
        override infix fun og(annen: PolicyResultat): PolicyResultat = if (annen == TILLAT) IKKE_AKTUELT else annen
        override infix fun eller(annen: PolicyResultat): PolicyResultat = if (annen == NEKT) IKKE_AKTUELT else annen
        override fun ikke(): PolicyResultat = IKKE_AKTUELT
    },
}
