package no.nav.hjelpemidler.database.jdbc

import java.sql.Connection
import javax.sql.DataSource

internal fun createSession(
    connection: Connection,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
): kotliquery.Session =
    kotliquery.Session(
        connection = kotliquery.Connection(connection),
        returnGeneratedKeys = returnGeneratedKeys,
        strict = strict,
        queryTimeout = queryTimeout
    )


internal fun createSession(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
): kotliquery.Session =
    createSession(
        connection = dataSource.connection,
        returnGeneratedKeys = returnGeneratedKeys,
        strict = strict,
        queryTimeout = queryTimeout
    )
