package no.nav.hjelpemidler.database

import javax.sql.DataSource

fun createDataSource(block: DataSourceConfiguration.() -> Unit = {}): DataSource =
    DataSourceConfiguration().apply(block).toDataSource()
