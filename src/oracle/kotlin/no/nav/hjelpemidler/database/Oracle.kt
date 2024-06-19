package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.collections.propertiesOf

object Oracle : DataSourceConfigurationFactory<OracleDataSourceConfiguration> {
    override fun invoke(block: OracleDataSourceConfiguration.() -> Unit): OracleDataSourceConfiguration =
        OracleDataSourceConfiguration()
            .apply(block)
            .apply {
                addDataSourcePropertyIfNotNull("url", jdbcUrl)
                addDataSourcePropertyIfNotNull("databaseName", databaseName)
                addDataSourceProperty(
                    "connectionProperties", propertiesOf(
                        "oracle.jdbc.defaultConnectionValidation" to "LOCAL",
                    )
                )
            }
}
