package no.nav.hjelpemidler.nare.policy

import no.nav.hjelpemidler.nare.core.LogiskOperand

enum class Policyavgjørelse : LogiskOperand<Policyavgjørelse> {
    TILLAT {
        override fun og(annen: Policyavgjørelse): Policyavgjørelse = annen
        override fun eller(annen: Policyavgjørelse): Policyavgjørelse = TILLAT
        override fun ikke(): Policyavgjørelse = NEKT
    },

    NEKT {
        override fun og(annen: Policyavgjørelse): Policyavgjørelse = NEKT
        override fun eller(annen: Policyavgjørelse): Policyavgjørelse = annen
        override fun ikke(): Policyavgjørelse = TILLAT
    },

    IKKE_AKTUELT {
        override fun og(annen: Policyavgjørelse): Policyavgjørelse = if (annen == TILLAT) IKKE_AKTUELT else annen
        override fun eller(annen: Policyavgjørelse): Policyavgjørelse = if (annen == NEKT) IKKE_AKTUELT else annen
        override fun ikke(): Policyavgjørelse = IKKE_AKTUELT
    },
}
