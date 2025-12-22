package no.nav.hjelpemidler.database

/**
 * @see [java.sql.DatabaseMetaData.getDatabaseProductName]
 */
enum class DatabaseVendor(val databaseProductName: String) {
    GENERIC(databaseProductName = ""),
    H2(databaseProductName = "H2"),
    ORACLE(databaseProductName = "Oracle"),
    POSTGRESQL(databaseProductName = "PostgreSQL"),
    ;

    val rowFactory: RowFactory
        get() = if (this == GENERIC) {
            GenericRowFactory
        } else {
            rowFactories[this] ?: error("Fant ikke RowFactory for $this")
        }

    companion object {
        fun of(databaseProductName: String): DatabaseVendor = entries.firstOrNull {
            it.databaseProductName == databaseProductName
        } ?: GENERIC
    }
}
