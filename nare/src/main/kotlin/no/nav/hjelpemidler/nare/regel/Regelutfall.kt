package no.nav.hjelpemidler.nare.regel

import no.nav.hjelpemidler.nare.core.LogiskOperand

enum class Regelutfall : LogiskOperand<Regelutfall> {
    JA {
        override fun og(annen: Regelutfall): Regelutfall = annen
        override fun eller(annen: Regelutfall): Regelutfall = JA
        override fun ikke(): Regelutfall = NEI
    },

    NEI {
        override fun og(annen: Regelutfall): Regelutfall = NEI
        override fun eller(annen: Regelutfall): Regelutfall = annen
        override fun ikke(): Regelutfall = JA
    },

    KANSKJE {
        override fun og(annen: Regelutfall): Regelutfall = if (annen == JA) KANSKJE else annen
        override fun eller(annen: Regelutfall): Regelutfall = if (annen == NEI) KANSKJE else annen
        override fun ikke(): Regelutfall = KANSKJE
    },
}
