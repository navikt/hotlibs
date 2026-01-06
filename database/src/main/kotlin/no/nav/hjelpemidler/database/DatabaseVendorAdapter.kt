package no.nav.hjelpemidler.database

import java.sql.ResultSet
import java.util.ServiceLoader

abstract class DatabaseVendorAdapter {
    abstract val vendor: DatabaseVendor

    abstract fun rowFactory(resultSet: ResultSet): Row
}

internal val databaseVendorAdapters: Map<DatabaseVendor, DatabaseVendorAdapter> by lazy {
    ServiceLoader
        .load(DatabaseVendorAdapter::class.java)
        .associateBy(DatabaseVendorAdapter::vendor)
}
