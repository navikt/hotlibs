package no.nav.hjelpemidler.database

object Testcontainers : DataSourceConfigurationFactory<TestcontainersDataSourceConfiguration> {
    override fun invoke(block: TestcontainersDataSourceConfiguration.() -> Unit): TestcontainersDataSourceConfiguration =
        TestcontainersDataSourceConfiguration()
            .apply(block)
            .apply {
                jdbcUrl = "jdbc:tc:postgresql:$tag:///${databaseName ?: "test"}?TC_TMPFS=/testtmpfs:rw&reWriteBatchedInserts=true"
            }
}
