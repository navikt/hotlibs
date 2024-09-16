package no.nav.hjelpemidler.database

object H2 : DataSourceConfigurationFactory<H2DataSourceConfiguration> {
    override fun invoke(block: H2DataSourceConfiguration.() -> Unit): H2DataSourceConfiguration =
        H2DataSourceConfiguration()
            .apply(block)
            .apply {
                val parameters = buildMap {
                    putAll(mode.parameters)
                    if (initScript != null) {
                        put("INIT", "RUNSCRIPT FROM 'classpath:$initScript'")
                    }
                }.map { (key, value) -> "$key=$value" }.joinToString(";")
                addDataSourceProperty("url", "jdbc:h2:mem:${databaseName ?: "test"};$parameters")
            }
}
