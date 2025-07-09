package no.nav.hjelpemidler.database

import com.zaxxer.hikari.HikariConfig
import no.nav.hjelpemidler.configuration.EnvironmentVariable

internal val HIKARI_MAXIMUM_POOL_SIZE: Int by EnvironmentVariable(defaultValue = 10)

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
