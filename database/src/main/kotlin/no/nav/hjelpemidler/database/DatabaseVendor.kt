package no.nav.hjelpemidler.database

/**
 * @see [java.sql.DatabaseMetaData.getDatabaseProductName]
 */
enum class DatabaseVendor(private val databaseProductName: String) {
    H2(databaseProductName = "H2"),
    ORACLE(databaseProductName = "Oracle"),
    POSTGRESQL(databaseProductName = "PostgreSQL"),
}
