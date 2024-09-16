package no.nav.hjelpemidler.database

import kotliquery.Session
import kotliquery.sessionOf
import javax.sql.DataSource

internal fun createSession(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
): Session = sessionOf(dataSource, returnGeneratedKeys, strict, queryTimeout)
