package no.nav.hjelpemidler.database

class H2DataSourceConfiguration internal constructor() : DataSourceConfiguration() {
    var mode: H2Mode = H2Mode.REGULAR
    var initScript: String? = null

    init {
        dataSourceClassName = "org.h2.jdbcx.JdbcDataSource"
    }
}
