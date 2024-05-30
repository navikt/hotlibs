package no.nav.hjelpemidler.database

import kotliquery.Session
import javax.sql.DataSource

/**
 * NB! Vurder å bruke [transactionAsync] i stedet. [transactionAsync] gir tilgang til [JdbcOperations] som
 * bare eksponerer de funksjonene vi trenger fremfor å bruke extensions på [Session].
 */
suspend fun <T> transaction(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    block: suspend (Session) -> T,
): T = withDatabaseContext {
    createSession(dataSource, returnGeneratedKeys, strict, queryTimeout).use { session ->
        session.transaction { tx -> block(tx) }
    }
}

suspend fun <T> transactionAsync(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
    block: suspend (JdbcOperations) -> T,
): T = withDatabaseContext {
    createSession(dataSource, returnGeneratedKeys, strict, queryTimeout).use { session ->
        session.transaction { tx -> block(tx.jdbcOperations) }
    }
}
