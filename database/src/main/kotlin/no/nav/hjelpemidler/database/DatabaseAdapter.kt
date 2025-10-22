package no.nav.hjelpemidler.database

import no.nav.hjelpemidler.service.loadService
import java.sql.ResultSetMetaData

interface DatabaseAdapter {
    fun handle(row: Row, columnIndex: Int, columnType: Int, metaData: ResultSetMetaData): Any?
}

val databaseAdapter: DatabaseAdapter by loadService<DatabaseAdapter>()
