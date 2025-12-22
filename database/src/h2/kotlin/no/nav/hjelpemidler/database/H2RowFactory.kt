package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.database.jdbc.H2ResultSet
import java.sql.ResultSet

internal class H2RowFactory : RowFactory {
    override val vendor: DatabaseVendor = DatabaseVendor.H2
    override fun invoke(resultSet: ResultSet): Row = Row(H2ResultSet(resultSet))
}
