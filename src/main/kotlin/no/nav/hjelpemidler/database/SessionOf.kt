package no.nav.hjelpemidler.database

import kotliquery.Session
import java.sql.Connection

fun sessionOf(
    connection: Connection,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
) = Session(
    connection = kotliquery.Connection(connection),
    returnGeneratedKeys = returnGeneratedKeys,
    strict = strict,
    queryTimeout = queryTimeout
)
