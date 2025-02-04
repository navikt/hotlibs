package no.nav.hjelpemidler.nare.evaluering

import no.nav.hjelpemidler.nare.core.LogiskOperand

enum class Resultat : LogiskOperand<Resultat> {
    JA {
        override infix fun og(annen: Resultat): Resultat = annen
        override infix fun eller(annen: Resultat): Resultat = JA
        override fun ikke(): Resultat = NEI
    },

    NEI {
        override infix fun og(annen: Resultat): Resultat = NEI
        override infix fun eller(annen: Resultat): Resultat = annen
        override fun ikke(): Resultat = JA
    },

    KANSKJE {
        override infix fun og(annen: Resultat): Resultat = if (annen == JA) KANSKJE else annen
        override infix fun eller(annen: Resultat): Resultat = if (annen == NEI) KANSKJE else annen
        override fun ikke(): Resultat = KANSKJE
    },
}
