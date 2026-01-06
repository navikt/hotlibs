package no.nav.hjelpemidler.database.generic

import no.nav.hjelpemidler.database.DatabaseVendor
import no.nav.hjelpemidler.database.DatabaseVendorAdapter
import no.nav.hjelpemidler.database.Row
import java.sql.ResultSet

internal class GenericAdapter : DatabaseVendorAdapter() {
    override val vendor: DatabaseVendor = DatabaseVendor.GENERIC
    override fun rowFactory(resultSet: ResultSet): Row = GenericRow(resultSet)
}
