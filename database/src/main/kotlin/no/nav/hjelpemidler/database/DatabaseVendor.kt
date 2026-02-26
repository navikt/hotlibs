package no.nav.hjelpemidler.database

/**
 * @see [java.sql.DatabaseMetaData.getDatabaseProductName]
 */
enum class DatabaseVendor(private val databaseProductName: String) {
    H2(databaseProductName = "H2"),
    ORACLE(databaseProductName = "Oracle"),
    POSTGRESQL(databaseProductName = "PostgreSQL"),
    ;

    internal val adapter: DatabaseVendorAdapter
        get() = databaseVendorAdapters[this] ?: error("Fant ikke DatabaseVendorAdapter for $this")

    companion object {
        internal fun of(databaseProductName: String): DatabaseVendor = entries.single {
            it.databaseProductName == databaseProductName
        }
    }
}
