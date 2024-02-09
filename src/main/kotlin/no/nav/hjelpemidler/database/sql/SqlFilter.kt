package no.nav.hjelpemidler.database.sql

import no.nav.hjelpemidler.database.QueryParameters

interface SqlFilter {
    val condition: SqlCondition

    val queryParameters: QueryParameters get() = emptyMap()
}
