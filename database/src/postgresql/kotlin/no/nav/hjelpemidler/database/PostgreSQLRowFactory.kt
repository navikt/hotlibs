package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.database.jdbc.PostgreSQLResultSet
import java.sql.ResultSet

internal class PostgreSQLRowFactory : RowFactory {
    override val vendor: DatabaseVendor = DatabaseVendor.POSTGRESQL
    override fun invoke(resultSet: ResultSet): Row = Row(PostgreSQLResultSet(resultSet))
}
