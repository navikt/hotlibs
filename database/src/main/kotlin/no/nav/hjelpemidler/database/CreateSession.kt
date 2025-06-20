package no.nav.hjelpemidler.database

import javax.sql.DataSource

internal fun createSession(
    dataSource: DataSource,
    returnGeneratedKeys: Boolean = false,
    strict: Boolean = true,
    queryTimeout: Int? = null,
): kotliquery.Session = kotliquery.sessionOf(dataSource, returnGeneratedKeys, strict, queryTimeout)
