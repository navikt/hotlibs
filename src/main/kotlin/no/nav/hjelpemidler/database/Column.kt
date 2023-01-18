package no.nav.hjelpemidler.database

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Column(
    val name: String,
)
