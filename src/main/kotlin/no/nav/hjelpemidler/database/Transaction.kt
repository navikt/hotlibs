package no.nav.hjelpemidler.database

import kotliquery.Session
import kotliquery.sessionOf
import javax.sql.DataSource

fun <T> transaction(
    dataSource: DataSource,
    returnGeneratedKey: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    block: (Session) -> T,
): T =
    sessionOf(
        dataSource = dataSource,
        returnGeneratedKey = returnGeneratedKey,
        strict = strict,
        queryTimeout = queryTimeout
    ).use { session ->
        session.transaction(block)
    }
