package no.nav.hjelpemidler.database

import kotliquery.Session
import javax.sql.DataSource

suspend fun <T> transaction(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    block: suspend (Session) -> T,
): T = withDatabaseContext {
    createSession(dataSource, returnGeneratedKeys, strict, queryTimeout).use { session ->
        session.transaction { tx ->
            block(tx)
        }
    }
}
