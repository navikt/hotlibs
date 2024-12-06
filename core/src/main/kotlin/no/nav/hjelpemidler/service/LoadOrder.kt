package no.nav.hjelpemidler.service

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class LoadOrder(val value: Int)
