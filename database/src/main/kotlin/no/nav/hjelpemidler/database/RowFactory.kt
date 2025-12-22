package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.database.jdbc.GenericResultSet
import java.sql.ResultSet
import java.util.ServiceLoader

interface RowFactory {
    val vendor: DatabaseVendor
    operator fun invoke(resultSet: ResultSet): Row
}

internal object GenericRowFactory : RowFactory {
    override val vendor: DatabaseVendor = DatabaseVendor.GENERIC
    override fun invoke(resultSet: ResultSet): Row = Row(GenericResultSet(resultSet))
}

internal val rowFactories: Map<DatabaseVendor, RowFactory> by lazy {
    ServiceLoader
        .load(RowFactory::class.java)
        .associateBy(RowFactory::vendor)
}
