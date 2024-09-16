package no.nav.hjelpemidler.database

/**
 * @see <a href="https://blogs.oracle.com/developers/post/hikaricp-best-practices-for-oracle-database-and-spring-boot">HikariCP Best Practices for Oracle Database and Spring Boot</a>
 */
class OracleDataSourceConfiguration internal constructor() : DataSourceConfiguration() {
    init {
        dataSourceClassName = "oracle.jdbc.pool.OracleDataSource"
    }
}
