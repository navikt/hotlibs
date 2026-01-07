package no.nav.hjelpemidler.database

/**
 * @see [java.sql.DatabaseMetaData.getDatabaseProductName]
 */
enum class DatabaseVendor(val databaseProductName: String) {
    H2(databaseProductName = "H2"),
    ORACLE(databaseProductName = "Oracle"),
    POSTGRESQL(databaseProductName = "PostgreSQL"),
    ;

    val adapter: DatabaseVendorAdapter
        get() = databaseVendorAdapters[this] ?: error("Fant ikke DatabaseVendorAdapter for $this")

    companion object {
        fun of(databaseProductName: String): DatabaseVendor = entries.single {
            it.databaseProductName == databaseProductName
        }
    }
}
