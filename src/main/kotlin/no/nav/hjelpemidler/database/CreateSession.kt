package no.nav.hjelpemidler.database

import kotliquery.sessionOf
import javax.sql.DataSource

fun createSession(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
): Session = sessionOf(dataSource, returnGeneratedKeys, strict, queryTimeout)
