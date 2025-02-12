package no.nav.hjelpemidler.nare.spesifikasjon

import no.nav.hjelpemidler.nare.core.LogiskOperand

interface Spesifikasjon<in T : Any, R : LogiskOperand<R>> {
    fun evaluer(context: T): R
}
