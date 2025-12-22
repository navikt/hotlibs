package no.nav.hjelpemidler.database.jdbc

import no.nav.hjelpemidler.database.DatabaseVendor
import java.sql.Connection

internal val Connection.vendor: DatabaseVendor get() = metaData.databaseProductName.let(DatabaseVendor::of)
