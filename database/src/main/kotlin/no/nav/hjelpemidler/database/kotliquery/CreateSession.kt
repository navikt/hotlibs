package no.nav.hjelpemidler.database.kotliquery

import javax.sql.DataSource

internal fun createSession(
    dataSource: DataSource,
    sessionProperties: SessionProperties,
): kotliquery.Session {
    val connection = dataSource.connection.apply { isReadOnly = sessionProperties.readOnly }
    return kotliquery.Session(
        connection = kotliquery.Connection(connection),
        returnGeneratedKeys = sessionProperties.returnGeneratedKeys,
        strict = sessionProperties.strict,
        queryTimeout = sessionProperties.queryTimeout,
    )
}
