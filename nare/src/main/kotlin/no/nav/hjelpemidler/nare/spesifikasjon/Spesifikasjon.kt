package no.nav.hjelpemidler.nare.spesifikasjon

interface Spesifikasjon<in T : Any, out R : Any> {
    fun evaluer(kontekst: T): R
}
