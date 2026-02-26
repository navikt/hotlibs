package no.nav.hjelpemidler.database.kotliquery

import javax.sql.DataSource

internal fun createSession(
    dataSource: DataSource,
    properties: SessionProperties,
): kotliquery.Session {
    val connection = dataSource.connection.apply { isReadOnly = properties.readOnly }
    return kotliquery.Session(
        connection = kotliquery.Connection(connection),
        returnGeneratedKeys = properties.returnGeneratedKeys,
        strict = properties.strict,
        queryTimeout = properties.queryTimeout,
    )
}
