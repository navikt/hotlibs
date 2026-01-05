package no.nav.hjelpemidler.database.postgresql

import no.nav.hjelpemidler.database.DatabaseVendor
import no.nav.hjelpemidler.database.DatabaseVendorAdapter
import no.nav.hjelpemidler.database.Row
import java.sql.ResultSet

internal class PostgreSQLAdapter : DatabaseVendorAdapter() {
    override val vendor: DatabaseVendor = DatabaseVendor.POSTGRESQL
    override fun rowFactory(resultSet: ResultSet): Row = PostgreSQLRow(resultSet)
}
