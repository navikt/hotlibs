package no.nav.hjelpemidler.nare.spesifikasjon

abstract class AbstractSpesifikasjon<T, U, R> {
    abstract fun evaluer(t: T): R

    abstract infix fun og(annen: Spesifikasjon<T>): Spesifikasjon<T>

    abstract infix fun eller(annen: Spesifikasjon<T>): Spesifikasjon<T>

    abstract fun ikke(): Spesifikasjon<T>

    abstract fun med(beskrivelse: String, id: String): Spesifikasjon<T>

    abstract fun toList(): List<Spesifikasjon<T>>
}
