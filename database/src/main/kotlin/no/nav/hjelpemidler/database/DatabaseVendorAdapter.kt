package no.nav.hjelpemidler.database

import java.sql.ResultSet
import java.util.ServiceLoader

abstract class DatabaseVendorAdapter {
    abstract val vendor: DatabaseVendor

    abstract fun rowFactory(resultSet: ResultSet): Row

    object Generic : DatabaseVendorAdapter() {
        override val vendor: DatabaseVendor = DatabaseVendor.GENERIC
        override fun rowFactory(resultSet: ResultSet): Row = GenericRow(resultSet)
    }
}

internal val databaseVendorAdapters: Map<DatabaseVendor, DatabaseVendorAdapter> by lazy {
    ServiceLoader
        .load(DatabaseVendorAdapter::class.java)
        .associateBy(DatabaseVendorAdapter::vendor)
        .plus(DatabaseVendor.GENERIC to DatabaseVendorAdapter.Generic)
}
