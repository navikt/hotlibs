package no.nav.hjelpemidler.database

import kotliquery.Connection
import kotliquery.Session

fun createSession(
    connection: java.sql.Connection,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
) = Session(
    connection = Connection(connection),
    returnGeneratedKeys = returnGeneratedKeys,
    strict = strict,
    queryTimeout = queryTimeout
)
