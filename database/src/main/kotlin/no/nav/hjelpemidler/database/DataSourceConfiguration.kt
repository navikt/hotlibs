package no.nav.hjelpemidler.database

import com.zaxxer.hikari.HikariConfig

abstract class DataSourceConfiguration : HikariConfig() {
    var databaseName: String? = null

    fun addDataSourcePropertyIfNotNull(propertyName: String, value: Any?) {
        if (value != null) {
            addDataSourceProperty(propertyName, value)
        }
    }
}
