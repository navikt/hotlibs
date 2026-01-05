package no.nav.hjelpemidler.database.oracle

import no.nav.hjelpemidler.database.DatabaseVendor
import no.nav.hjelpemidler.database.DatabaseVendorAdapter
import no.nav.hjelpemidler.database.Row
import java.sql.ResultSet

internal class OracleAdapter : DatabaseVendorAdapter() {
    override val vendor: DatabaseVendor = DatabaseVendor.ORACLE
    override fun rowFactory(resultSet: ResultSet): Row = OracleRow(resultSet)
}
