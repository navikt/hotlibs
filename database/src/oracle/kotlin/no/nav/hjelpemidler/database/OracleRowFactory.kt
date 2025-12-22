package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.database.jdbc.OracleResultSet
import java.sql.ResultSet

internal class OracleRowFactory : RowFactory {
    override val vendor: DatabaseVendor = DatabaseVendor.ORACLE
    override fun invoke(resultSet: ResultSet): Row = Row(OracleResultSet(resultSet))
}
