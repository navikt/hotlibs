package no.nav.hjelpemidler.database

import kotliquery.Session
import javax.sql.DataSource

fun <T> session(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    block: (Session) -> T,
): T = createSession(dataSource, returnGeneratedKeys, strict, queryTimeout).use { session ->
    block(session)
}
