package no.nav.hjelpemidler.database.jdbc

import java.sql.Connection
import javax.sql.DataSource

internal fun createSession(
    connection: Connection,
    readOnly: Boolean = false,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
): kotliquery.Session =
    kotliquery.Session(
        connection = kotliquery.Connection(connection.apply { isReadOnly = readOnly }),
        returnGeneratedKeys = returnGeneratedKeys,
        strict = strict,
        queryTimeout = queryTimeout,
    )


internal fun createSession(
    dataSource: DataSource,
    readOnly: Boolean = false,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
): kotliquery.Session =
    createSession(
        connection = dataSource.connection,
        readOnly = readOnly,
        returnGeneratedKeys = returnGeneratedKeys,
        strict = strict,
        queryTimeout = queryTimeout,
    )
