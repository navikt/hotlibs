package no.nav.hjelpemidler.database

import kotliquery.Session
import kotliquery.sessionOf
import javax.sql.DataSource

fun <T> transaction(dataSource: DataSource, block: (Session) -> T): T =
    sessionOf(dataSource = dataSource, returnGeneratedKey = true, strict = true)
        .use { session ->
            session.transaction(block)
        }
