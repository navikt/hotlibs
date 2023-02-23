package no.nav.hjelpemidler.database

import org.postgresql.jdbcurlresolver.PgPassParser

sealed interface DatabaseConfiguration {
    val hostname: String
    val port: String
    val database: String
    val username: String
    val password: String
    val jdbcUrl: String
    val driverClassName: String
    val cleanDisabled: Boolean get() = true

    data class PostgreSQL(
        override val hostname: String,
        override val port: String,
        override val database: String,
        override val username: String,
        override val password: String = requireNotNull(PgPassParser.getPassword(hostname, port, database, username)) {
            "Kunne ikke utlede passord fra .pgpass"
        },
        override val cleanDisabled: Boolean = true,
    ) : DatabaseConfiguration {
        override val jdbcUrl = "jdbc:postgresql://$hostname:$port/$database?reWriteBatchedInserts=true"
        override val driverClassName: String = "org.postgresql.Driver"
    }
}
