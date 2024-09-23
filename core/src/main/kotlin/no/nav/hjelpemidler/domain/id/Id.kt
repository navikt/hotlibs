package no.nav.hjelpemidler.domain.id

/**
 * Interface for implementasjon av sterke typer for ulike identifikatorer.
 */
interface Id<out T : Any> {
    val value: T
}
