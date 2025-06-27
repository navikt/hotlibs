package no.nav.hjelpemidler.database

import com.zaxxer.hikari.HikariConfig
import no.nav.hjelpemidler.configuration.EnvironmentVariable

internal val HIKARI_MAXIMUM_POOL_SIZE by EnvironmentVariable(
    defaultValue = 10,
    transform = String::toInt,
)

abstract class DataSourceConfiguration : HikariConfig() {
    var databaseName: String? = null

    init {
        maximumPoolSize = HIKARI_MAXIMUM_POOL_SIZE
    }

    fun addDataSourcePropertyIfNotNull(propertyName: String, value: Any?) {
        if (value != null) {
            addDataSourceProperty(propertyName, value)
        }
    }
}
