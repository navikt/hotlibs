package no.nav.hjelpemidler.configuration

/**
 * Brukes for å markere at en egenskap er definert utenfor applikasjonen.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class External
