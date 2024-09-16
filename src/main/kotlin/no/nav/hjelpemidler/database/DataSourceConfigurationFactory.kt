package no.nav.hjelpemidler.database

fun interface DataSourceConfigurationFactory<T : DataSourceConfiguration> {
    operator fun invoke(block: T.() -> Unit): T
}
