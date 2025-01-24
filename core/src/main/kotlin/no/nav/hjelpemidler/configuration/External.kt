package no.nav.hjelpemidler.configuration

/**
 * Brukes for å markere at en egenskap/miljøvariabel er definert utenfor applikasjonen.
 * F.eks. fra en mounted secret eller for database/Kafka.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class External(val secret: String = "")
