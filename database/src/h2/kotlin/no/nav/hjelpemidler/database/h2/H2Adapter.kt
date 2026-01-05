package no.nav.hjelpemidler.database.h2

import no.nav.hjelpemidler.database.DatabaseVendor
import no.nav.hjelpemidler.database.DatabaseVendorAdapter
import no.nav.hjelpemidler.database.Row
import java.sql.ResultSet

internal class H2Adapter : DatabaseVendorAdapter() {
    override val vendor: DatabaseVendor = DatabaseVendor.H2
    override fun rowFactory(resultSet: ResultSet): Row = H2Row(resultSet)
}
