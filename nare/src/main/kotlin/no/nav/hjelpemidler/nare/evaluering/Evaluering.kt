package no.nav.hjelpemidler.nare.evaluering

import no.nav.hjelpemidler.nare.core.LogiskOperand

interface Evaluering<T : LogiskOperand<T>> {
    val resultat: T
    val begrunnelse: String
    val operator: Operator
}
