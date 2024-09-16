package no.nav.hjelpemidler.database

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

fun <T : DataSourceConfiguration> createDataSource(
    configurationFactory: DataSourceConfigurationFactory<T>,
    block: T.() -> Unit = {},
): DataSource = HikariDataSource(configurationFactory(block))
