package no.nav.hjelpemidler.database

import com.zaxxer.hikari.HikariConfig

abstract class DataSourceConfiguration : HikariConfig() {
    val cluster: String by lazy { System.getenv("NAIS_CLUSTER_NAME") ?: "local" }
    var databaseName: String? = null

    fun addDataSourcePropertyIfNotNull(propertyName: String, value: Any?) {
        if (value != null) {
            addDataSourceProperty(propertyName, value)
        }
    }
}
