package no.nav.hjelpemidler.database

import java.sql.Connection

internal val Connection.vendor: DatabaseVendor get() = metaData.databaseProductName.let(DatabaseVendor::of)
