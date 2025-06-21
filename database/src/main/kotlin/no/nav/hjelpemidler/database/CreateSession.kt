package no.nav.hjelpemidler.database

import kotliquery.Connection
import javax.sql.DataSource

internal fun createSession(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
): kotliquery.Session =
    kotliquery.Session(
        connection = Connection(dataSource.connection),
        returnGeneratedKeys = returnGeneratedKeys,
        strict = strict,
        queryTimeout = queryTimeout
    )
